

--This sends us back to the galaxy screen.
--setRenderView is case insensitive.
--gameState is a reference to the currently active game state.
--widget is the widget that has been touched (in this case, our ScriptableTextButton).
function galaxy_screen_btn_on_click(widget, game_instance, game_state)
    print('[Lua Debug] Galaxy screen button clicked!')
    game_instance:getCameraManager():setRenderView("GALACTIC");
end

--This updates the system text label on the scripted GUI screen.
--If no star system is selected, we just set the value to the default,
--But if a star system is selected, we set the text appropriately.
function system_name_label_on_show(widget, game_instance, game_state)
    print('[Lua Debug] System name label showing...')
    if game_instance:isSystemSelected() then
        widget:setText('Star System #X');
        print('No system selected.')
    else
        print('System selected')
        widget:setText('Star System # ' .. game_instance:getSelectedStarSystem().id);
    end
end

function system_name_label_on_hide(widget, game_instance, game_state)
    print('[Lua Debug] System name label hiding...')
end

function visit_planet_btn_on_show(widget, game_instance, game_state)
end

function visit_planet_btn_on_hide(widget, game_instance, game_state)
end

function visit_planet_btn_on_click(widget, game_instance, game_state)
end
