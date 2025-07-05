package com.minutestilldawn.game.Controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.minutestilldawn.game.Main;
import com.minutestilldawn.game.Model.*;

public class GameController extends InputAdapter {

    private final Main gameInstance;
    private final GameState gameState;
    private final GameAssetManager assetManager;
    private final Player player;
    private final OrthographicCamera gameCamera; // For converting screen to world coords

    private final Array<Enemy> activeEnemies;
    private final Pool<Enemy> enemyPool;

    private final Array<Bullet> activePlayerBullets;
    private final Pool<Bullet> playerBulletPool;
    private final Array<Bullet> activeEnemyBullets;
    private final Pool<Bullet> enemyBulletPool;

    private final Array<Tree> trees; // Static obstacles

    // Enemy Spawning Timers & Logic
    private float tentacleSpawnTimer;
    private static final float TENTACLE_SPAWN_INTERVAL_INITIAL = 3.0f; // PDF: every 3s if 2s passed
    private float eyebatSpawnTimer;
    private static final float EYEBAT_SPAWN_INTERVAL_INITIAL = 10.0f; // PDF: every 10s if 30s passed
    private boolean elderBossSpawned = false;

    private Vector2 mouseWorldPos = new Vector2(); // To store world coordinates of mouse

    // Auto-aim
    private Enemy autoAimTarget = null;

    public GameController(Main gameInstance, GameState gameState, GameAssetManager assetManager,
            OrthographicCamera gameCamera) {
        this.gameInstance = gameInstance;
        this.gameState = gameState;
        this.assetManager = assetManager;
        this.player = gameState.getPlayerInstance();
        this.gameCamera = gameCamera;

        activeEnemies = new Array<>();
        enemyPool = new Pool<Enemy>() {
            @Override
            protected Enemy newObject() {
                return new Enemy();
            }
        };

        activePlayerBullets = new Array<>();
        playerBulletPool = new Pool<Bullet>() {
            @Override
            protected Bullet newObject() {
                return new Bullet();
            }
        };
        activeEnemyBullets = new Array<>();
        enemyBulletPool = new Pool<Bullet>() {
            @Override
            protected Bullet newObject() {
                return new Bullet();
            }
        };

        trees = new Array<>();
        initializeLevel(); // Spawn initial trees
    }

    private void initializeLevel() {
        // Spawn initial trees randomly (PDF: "در ابتدای شروع کردن بازی به صورت رندوم در
        // جاهای مختلف قرار گیرند")
        int numTrees = 10; // Example number
        Texture treeTexture = assetManager.getTreeFrames()[0];
        for (int i = 0; i < numTrees; i++) {
            // Spawn within a certain area, avoiding player start position
            float x = MathUtils.random(-500, 1300); // Example world bounds
            float y = MathUtils.random(-300, 900);
            // Ensure trees are not too close to player start (400,300)
            if (Vector2.dst(x, y, player.getPosition().x, player.getPosition().y) > 100) {
                trees.add(new Tree(x, y, treeTexture));
            } else {
                i--; // try again
            }
        }
    }

    public void update(float delta) {
        if (gameState.getCurrentStatus() != GameStatus.PLAYING) {
            return; // Don't update game logic if paused or game over
        }

        handleInput(delta); // Player movement and aiming
        player.update(delta);

        // Update mouse world position for aiming
        Vector3 screenMousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        gameCamera.unproject(screenMousePos);
        mouseWorldPos.set(screenMousePos.x, screenMousePos.y);
        player.setAimDirection(new Vector2(mouseWorldPos).sub(player.getPosition()));

        if (gameState.isAutoAimActive()) {
            findAutoAimTarget();
        } else {
            autoAimTarget = null;
        }

        // Enemy Spawning
        spawnEnemies(delta);

        // Update Enemies
        for (int i = activeEnemies.size - 1; i >= 0; i--) {
            Enemy enemy = activeEnemies.get(i);
            enemy.update(delta, player.getPosition(), enemyBulletPool, activeEnemyBullets, assetManager);
            if (!enemy.isActive()) {
                player.gainXP(getXpForEnemy(enemy.getType())); // Grant XP
                // TODO: Spawn XP seed visual
                activeEnemies.removeIndex(i);
                enemyPool.free(enemy);
                gameState.incrementKills();
            }
        }

        // Update Player Bullets
        for (int i = activePlayerBullets.size - 1; i >= 0; i--) {
            Bullet bullet = activePlayerBullets.get(i);
            bullet.update(delta);
            if (!bullet.isActive() || isOutOfWorld(bullet.getPosition())) {
                activePlayerBullets.removeIndex(i);
                playerBulletPool.free(bullet);
            }
        }
        // Update Enemy Bullets
        for (int i = activeEnemyBullets.size - 1; i >= 0; i--) {
            Bullet bullet = activeEnemyBullets.get(i);
            bullet.update(delta);
            if (!bullet.isActive() || isOutOfWorld(bullet.getPosition())) {
                activeEnemyBullets.removeIndex(i);
                enemyBulletPool.free(bullet);
            }
        }

        handleCollisions();

        // Check for level up
        if (player.canLevelUp() && gameState.getCurrentStatus() == GameStatus.PLAYING) {
            gameState.setCurrentStatus(GameStatus.LEVEL_UP_CHOICE);
            // GameScreenView will detect this change and show the level up UI
        }

        // Check game over conditions
        if (player.getCurrentHp() <= 0) {
            gameState.setCurrentStatus(GameStatus.GAME_OVER_LOSE);
            gameInstance.handleGameEnd(gameState);
        } else if (gameState.getTimeRemainingSeconds() <= 0 && gameState.getCurrentStatus() == GameStatus.PLAYING) {
            // GameState.update already sets status to GAME_OVER_WIN
            gameInstance.handleGameEnd(gameState);
        }
    }

    private void handleInput(float delta) {
        Vector2 moveDirection = new Vector2();
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP))
            moveDirection.y += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN))
            moveDirection.y -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT))
            moveDirection.x -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            moveDirection.x += 1;

        if (moveDirection.len2() > 0) { // If there's any movement input
            player.move(moveDirection.nor(), delta); // nor() normalizes the vector
        }

        // Shooting is handled by touchDown/mouseClicked event for more precise timing
        // but could also be polled for continuous fire if desired.
    }

    private void findAutoAimTarget() {
        autoAimTarget = null;
        float closestDistSq = Float.MAX_VALUE;
        for (Enemy enemy : activeEnemies) {
            if (enemy.isActive()) {
                float distSq = player.getPosition().dst2(enemy.getPosition());
                if (distSq < closestDistSq) {
                    closestDistSq = distSq;
                    autoAimTarget = enemy;
                }
            }
        }
    }

    private void spawnEnemies(float delta) {
        float elapsedTime = gameState.getElapsedTimeSeconds();
        float spawnRateModifier = 1.0f - (elapsedTime / (gameState.getChosenGameDurationSeconds() * 2f)); // Faster over
                                                                                                          // time
        spawnRateModifier = Math.max(0.25f, spawnRateModifier); // Cap minimum spawn interval reduction

        // Tentacle Monster Spawning (PDF: after 2s, every 3s)
        if (elapsedTime >= 2.0f) {
            tentacleSpawnTimer -= delta;
            if (tentacleSpawnTimer <= 0) {
                tentacleSpawnTimer = TENTACLE_SPAWN_INTERVAL_INITIAL * spawnRateModifier;
                spawnEnemy(Enemy.EnemyType.TENTACLE_MONSTER);
            }
        }

        // Eyebat Spawning (PDF: after 30s, every 10s)
        // "اگر زمان کل بازی 1 ثانیه باشد پس از گذشت - ثانیه از زمان بازی شروع به اسپان
        // میکند و اگر ثانیه از زمان بازی گذشته باشد 30 دشمن از این نوع هر ۱۰ ثانیه
        // اسپان میشود"
        // This is a bit confusing. Assuming "اگر 30 ثانیه از زمان بازی گذشته باشد"
        // means after 30s of game time.
        if (elapsedTime >= 30.0f) {
            eyebatSpawnTimer -= delta;
            if (eyebatSpawnTimer <= 0) {
                eyebatSpawnTimer = EYEBAT_SPAWN_INTERVAL_INITIAL * spawnRateModifier;
                spawnEnemy(Enemy.EnemyType.EYEBAT);
            }
        }

        // Elder Boss Spawning (PDF: after half game duration)
        if (!elderBossSpawned && elapsedTime >= gameState.getChosenGameDurationSeconds() / 2f) {
            spawnEnemy(Enemy.EnemyType.ELDER_BOSS);
            elderBossSpawned = true;
            Gdx.app.log("GameController", "ELDER BOSS HAS SPAWNED!");
            // TODO: Add logic for the shrinking arena if implementing boss fight fully
        }
    }

    private void spawnEnemy(Enemy.EnemyType type) {
        Enemy enemy = enemyPool.obtain();
        // Spawn outside camera view
        Vector2 spawnPos = getOffScreenSpawnPosition();
        TextureRegion texture = assetManager.getEnemyTexture(type);
        enemy.init(type, spawnPos.x, spawnPos.y, texture, assetManager);
        activeEnemies.add(enemy);
    }

    private Vector2 getOffScreenSpawnPosition() {
        float x = 0, y = 0;
        float halfWidth = gameCamera.viewportWidth / 2 * 1.1f; // Slightly outside view
        float halfHeight = gameCamera.viewportHeight / 2 * 1.1f;
        float playerX = player.getPosition().x;
        float playerY = player.getPosition().y;

        int side = MathUtils.random(3); // 0: top, 1: bottom, 2: left, 3: right
        if (side == 0) { // Top
            x = MathUtils.random(playerX - halfWidth, playerX + halfWidth);
            y = playerY + halfHeight;
        } else if (side == 1) { // Bottom
            x = MathUtils.random(playerX - halfWidth, playerX + halfWidth);
            y = playerY - halfHeight;
        } else if (side == 2) { // Left
            x = playerX - halfWidth;
            y = MathUtils.random(playerY - halfHeight, playerY + halfHeight);
        } else { // Right
            x = playerX + halfWidth;
            y = MathUtils.random(playerY - halfHeight, playerY + halfHeight);
        }
        return new Vector2(x, y);
    }

    private void handleCollisions() {
        // Player Bullets vs Enemies
        for (int i = activePlayerBullets.size - 1; i >= 0; i--) {
            Bullet bullet = activePlayerBullets.get(i);
            if (!bullet.isActive())
                continue;
            for (int j = activeEnemies.size - 1; j >= 0; j--) {
                Enemy enemy = activeEnemies.get(j);
                if (!enemy.isActive())
                    continue;
                if (Intersector.overlaps(bullet.getBounds(), enemy.getBounds())) {
                    enemy.takeDamage(bullet.getDamage());
                    bullet.setActive(false); // Bullet is used up
                    // TODO: Add hit effect
                    break; // Bullet hits one enemy
                }
            }
        }

        // Enemy Bullets vs Player
        for (int i = activeEnemyBullets.size - 1; i >= 0; i--) {
            Bullet bullet = activeEnemyBullets.get(i);
            if (!bullet.isActive())
                continue;
            if (Intersector.overlaps(bullet.getBounds(), player.getCharacterType().getHitbox(player.getPosition()))) {
                player.takeDamage(bullet.getDamage());
                bullet.setActive(false);
                // TODO: Add player hit effect
            }
        }

        // Player vs Enemies
        if (!player.isInvincible()) {
            for (Enemy enemy : activeEnemies) {
                if (!enemy.isActive())
                    continue;
                if (Intersector.overlaps(player.getCharacterType().getHitbox(player.getPosition()),
                        enemy.getBounds())) {
                    player.takeDamage(10); // Example contact damage
                    // TODO: Add collision effect, maybe small knockback
                    break; // Player takes damage from one enemy at a time to prevent instant death
                }
            }
        }

        // Player vs Trees
        if (!player.isInvincible()) {
            for (Tree tree : trees) {
                if (Intersector.overlaps(player.getCharacterType().getHitbox(player.getPosition()), tree.getBounds())) {
                    player.takeDamage(Tree.DAMAGE_ON_CONTACT);
                    // Player becomes invincible, so won't take damage again immediately
                    break;
                }
            }
        }
    }

    private boolean isOutOfWorld(Vector2 pos) {
        // Define world boundaries based on camera and some margin
        float margin = 200f;
        float minX = gameCamera.position.x - gameCamera.viewportWidth / 2 - margin;
        float maxX = gameCamera.position.x + gameCamera.viewportWidth / 2 + margin;
        float minY = gameCamera.position.y - gameCamera.viewportHeight / 2 - margin;
        float maxY = gameCamera.position.y + gameCamera.viewportHeight / 2 + margin;
        return pos.x < minX || pos.x > maxX || pos.y < minY || pos.y > maxY;
    }

    private int getXpForEnemy(Enemy.EnemyType type) {
        // PDF: "به ازای هر دانه ۳ XP" - assuming each enemy drops one "seed" equivalent
        return 3;
    }

    /**
     * Called by GameScreenView when player makes an ability choice.
     */
    public void processAbilityChoice(Ability ability) {
        player.activateAbility(ability);
        player.levelUp(); // Finalize level up after ability is chosen
        gameState.setCurrentStatus(GameStatus.PLAYING);
    }

    // --- InputAdapter Overrides ---
    @Override
    public boolean keyDown(int keycode) {
        if (gameState.getCurrentStatus() != GameStatus.PLAYING)
            return false;

        if (keycode == Input.Keys.R) {
            player.reloadWeapon();
            return true;
        }
        if (keycode == Input.Keys.SPACE) { // Toggle Auto-Aim
            gameState.toggleAutoAim();
            Gdx.app.log("GameController", "Auto-Aim: " + (gameState.isAutoAimActive() ? "ON" : "OFF"));
            return true;
        }
        // For cheat codes (PDF Section 3)
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT)) {
            if (keycode == Input.Keys.NUM_1) { // Reduce time by 1 minute
                gameState.cheatReduceTime(60);
                Gdx.app.log("CHEAT", "Time reduced by 1 minute.");
                return true;
            }
            if (keycode == Input.Keys.NUM_2) { // Increase player level
                player.gainXP(player.getXpNeededForNextLevel() - player.getXp() + 1); // Give enough XP to level up
                // Level up choice will be triggered on next update if status is PLAYING
                Gdx.app.log("CHEAT", "XP added to trigger level up.");
                return true;
            }
            if (keycode == Input.Keys.NUM_3) { // Heal player if HP not full
                if (player.getCurrentHp() < player.getMaxHp()) {
                    player.setCurrentHp(player.getMaxHp());
                    Gdx.app.log("CHEAT", "Player healed.");
                } else {
                    Gdx.app.log("CHEAT", "Player already at full HP.");
                }
                return true;
            }
            if (keycode == Input.Keys.NUM_4) { // Go to boss fight (spawn boss)
                if (!elderBossSpawned) {
                    spawnEnemy(Enemy.EnemyType.ELDER_BOSS);
                    elderBossSpawned = true;
                    Gdx.app.log("CHEAT", "Elder Boss spawned.");
                } else {
                    Gdx.app.log("CHEAT", "Elder Boss already spawned or conditions not met.");
                }
                return true;
            }
            if (keycode == Input.Keys.NUM_5) { // Custom Cheat: Grant 50 XP
                player.gainXP(50);
                Gdx.app.log("CHEAT", "Granted 50 XP.");
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (gameState.getCurrentStatus() != GameStatus.PLAYING)
            return false;

        if (button == Input.Buttons.LEFT) {
            player.shoot(mouseWorldPos, gameState.isAutoAimActive(),
                    autoAimTarget != null ? autoAimTarget.getPosition() : null, playerBulletPool, activePlayerBullets,
                    assetManager);
            return true;
        }
        return false;
    }

    // Getters for GameScreenView to render entities
    public Array<Enemy> getActiveEnemies() {
        return activeEnemies;
    }

    public Array<Bullet> getActivePlayerBullets() {
        return activePlayerBullets;
    }

    public Array<Bullet> getActiveEnemyBullets() {
        return activeEnemyBullets;
    }

    public Array<Tree> getTrees() {
        return trees;
    }

    public Enemy getAutoAimTarget() {
        return autoAimTarget;
    }

}
