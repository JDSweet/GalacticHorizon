package org.origin.spacegame.ai;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import org.luaj.vm2.LuaValue;

/*
*
* This class takes a lua context and uses pre-defined functions in the lua context
* to evaluate itself. By default, the ScriptedState evaluates against a specific object
* of type E (which is passed to the script functions).
* For example:
*   function <state_id>_on_enter(ai_state, ai_entity, game_instance, game_state)
*
*   end
* Scripted States can be defined in and loaded from XML files.
* <AIState category = "system_fleet_strategic" id = "attack_nearby_enemy_ships"/>
* <AIState category = "system_fleet_strategic" id = "defend_nearby_stations"/>
* <AIState category = "system_fleet_strategic" id = "defend_nearby_planets" />
* <AIState category = "system_fleet_strategic" id = "defend_nearby_transports" />
* <AIState category = "system_fleet_strategic" id = "patrol_system" />
* */
public class ScriptedState<E> implements State<E>
{
    protected LuaValue onEnterStateCallback;
    protected LuaValue onUpdateStateCallback;
    protected LuaValue onExitStateCallback;
    protected LuaValue onReceiveMessageCallback;

    public ScriptedState(String state_name, LuaValue ctxt)
    {
        this.onEnterStateCallback = ctxt.get(state_name + "_on_enter");
        this.onUpdateStateCallback = ctxt.get(state_name + "_on_update");
        this.onExitStateCallback = ctxt.get(state_name + "_on_exit");
        this.onReceiveMessageCallback = ctxt.get(state_name + "_on_message");
    }

    @Override
    public void enter(E e)
    {
        this.onEnterStateCallback.invoke();
    }

    @Override
    public void update(E e)
    {
        this.onUpdateStateCallback.invoke();
    }

    @Override
    public void exit(E e)
    {
        this.onExitStateCallback.invoke();
    }

    @Override
    public boolean onMessage(E e, Telegram telegram)
    {
        this.onReceiveMessageCallback.invoke();
        return true;
    }
}
