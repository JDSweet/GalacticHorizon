

function ship_rotate_and_shoot_nearest_target_state_on_enter(ship, game_instance, game_state)
    --print('[05_ship_rotate_and_shoot_target_state.on_enter] Begining combat!' .. at_location_dst)
end

function ship_rotate_and_shoot_nearest_target_state_on_update(ship, game_instance, game_state)
    --Flies away from nearest ship until it leaves attack range.
    if game_instance:hasString("game_mode") and game_instance:getString("game_mode") == "ship_mode_move" then
        local target = ship:getTarget()
        local target_position = target:getPosition()
        print('[ScriptingDebug: 05_ship_rotate_and_shoot_nearest_target_state.on_update]: Ship ' .. ship:getID() .. ' has completed its evasion calculations...')
        if target ~= nil then
            ship:turnAway(target_position)
            ship:thrust(baseThrustValue)
            print('[ScriptingDebug: 05_ship_rotate_and_shoot_nearest_target_state.on_update]: Ship ' .. ship:getID() .. ' is evading enemy...')
        end
    end
end

function ship_rotate_and_shoot_nearest_target_state_on_exit(ship, game_instance, game_state)
end

function ship_rotate_and_shoot_nearest_target_state_on_receive_message(ship, message, game_instance, game_state)
end
