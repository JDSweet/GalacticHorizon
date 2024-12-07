

--This sends us back to the galaxy screen.
--setRenderView is case insensitive.
--gameState is a reference to the currently active game state.
--widget is the widget that has been touched (in this case, our ScriptableTextButton).
function galaxy_screen_btn_on_click(widget, game_instance, game_state)
    --print('Button clicked!')
    game_instance:getCameraManager():setRenderView("GALACTIC")
end
