-- Note: The biggest issue I've experienced so far has been making sure the lua scripts are making
-- the correct API calls.
--
-- Java -> Lua Intellisense in IntelliJ doesn't work, so all Lua -> Java API calls have to be referenced
--
-- If you try to make non-existent API calls in a script, that script will cause the game to crash randomly
-- and the error logs don't give you much to work with except "your program is trying to call a nil value"
-- You need to make sure the functions you call in lua are the same ones defined in Java,
-- and this is tedious, because it involves a lot of cross-checking between code and documentation.

baseThrustValue = 12
at_location_dst = 4
orbit_enemy_dst = at_location_dst

function ship_approach_nearest_target_state_on_enter(ship, game_instance, game_state)

end

-- IT WORKS!!! NO BUGS!!! HALLELUJAH!!!
-- We're just approaching the enemy ship that we selected in the search_nearest_target state. We stop if we've arrived,
-- Then we transition into the "ship_rotate_and_shoot" state, where we'll just be picking some random point around the enemy ship and thrusting to it.
-- After weapons are implemented, that state will also cause us to attack the enemy ship we've targeted while we move.
function ship_approach_nearest_target_state_on_update(ship, game_instance, game_state)
    if game_instance:hasString("game_mode") and game_instance:getString("game_mode") == "ship_mode_move" then
        local pos = game_instance:vec2()
        pos:set(ship:getTarget():getPosition().x, ship:getTarget():getPosition().y)
        print('[ScriptingDebug: 04_ship_approach_nearest_target_state.on_update] Approaching target ' .. ship:getTarget():getID())
        ship:turnTowards(pos)
        ship:thrust(baseThrustValue)
        if ship:dstToShip(ship:getTarget()) < at_location_dst then
            ship:stop()
            ship:setAtDestination(true)
            print('[ScriptingDebug: 04_ship_approach_nearest_target_state.on_update] We have arrived!')
        end
        -- If the ship has arrived at its destination, we're going to change states,
        -- and start circling/shooting at the target ship.
        if ship:isAtDestination() then
            print('[ScriptingDebug: 04_ship_approach_nearest_target_state.on_update] Ship has arrived at its destination. Preparing for combat!')
            ship:getStateMachine():changeState(game_instance:getShipAIState('ship_rotate_and_shoot_nearest_target_state'))
        end
    -- If we don't manually stop the ship, it will continue moving towards its destination when we change the game_mode,
    -- because the logic that stops the ship is contained within that conditional.
    elseif game_instance:hasString("game_mode") and game_instance:getString("game_mode") == "ship_mode_spawn" then
        ship:stop()
    end
end

function ship_approach_nearest_target_state_on_exit(ship, game_instance, game_state)
    --ship:setAtDestination(false) -- When we leave to another state, we're going to say we're no longer at our destination.
end

function ship_approach_nearest_target_state_on_receive_message(ship, message, game_instance, game_state)
end
