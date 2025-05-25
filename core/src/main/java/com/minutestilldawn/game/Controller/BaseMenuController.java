package com.minutestilldawn.game.Controller;

import com.minutestilldawn.game.View.BaseMenuView;

public abstract class BaseMenuController {

    BaseMenuView  view; 
    

    public void setView(BaseMenuView view) {
        this.view = view;
    }

    // Optionally hold a reference to the view
    // Define abstract methods for handling button events
    public abstract void onButtonClicked(String buttonId);

    public void handleButtons(){
        
    }
}