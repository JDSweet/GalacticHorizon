

function ship_rotate_and_shoot_nearest_target_state_on_enter(ship, game_instance, game_state)
    --print('[05_ship_rotate_and_shoot_target_state.on_enter] Begining combat!' .. at_location_dst)
end

function ship_rotate_and_shoot_nearest_target_state_on_update(ship, game_instance, game_state)
    --Pick a random point within the radius of at_location_dst
    if game_instance:hasString("game_mode") and game_instance:getString("game_mode") == "ship_mode_move" then
        local target = ship:getTarget()
        --Pick a random point in the system and start moving towards it.
        --Once we leave range of the target, start moving towards the target again.
    end
end

function ship_rotate_and_shoot_nearest_target_state_on_exit(ship, game_instance, game_state)
end

function ship_rotate_and_shoot_nearest_target_state_on_receive_message(ship, message, game_instance, game_state)
end
