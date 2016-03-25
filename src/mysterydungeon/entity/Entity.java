/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.entity;

import java.util.ArrayList;
import mysterydungeon.MysteryDungeon;
import mysterydungeon.DungeonComp;
import mysterydungeon.dungeon.Dungeon;
import mysterydungeon.dungeon.Node;
import mysterydungeon.dungeon.RoomNode;
import mysterydungeon.move.Move;

/**
 *
 * @author Justis
 */
public class Entity
{
    private Node currentNode;
    private Node gotoNode;
    private Dungeon dungeon;
    private Species species;
    private EntityState currentState;
    private int currentX;
    private int currentY;
    private boolean moving;
    private String name;
    private boolean isPlayer;

    private int currentHP;
    private int maxHP;

    private ArrayList<Move> knownMoves;

    public int facing;

    public Entity(Dungeon dungeon, Species species, EntityState startState, boolean player)
    {
        this.dungeon = dungeon;
        this.species = species;
        this.name = species.getName();
        this.isPlayer = player;
        maxHP = species.getHP();
        currentHP = maxHP;
        knownMoves = (ArrayList<Move>)species.getMoves().clone(); //Clone so we don't modify the actual species data.
        randomizeLocation();
        currentState = startState;
    }

    public Entity(Dungeon dungeon, Species species, boolean player)
    {
        this(dungeon, species, new MoveState(), player);
    }

    public Entity(Dungeon dungeon, Species species)
    {
        this(dungeon, species, new MoveState(), false);
    }

    private Node generateLocation()
    {
        ArrayList<RoomNode> rooms = dungeon.getRooms();
        int randomRoom = Dungeon.PRNG.nextInt(rooms.size());
        RoomNode room = rooms.get(randomRoom);
        ArrayList<Node> nodes = room.getNodes();
        int randomNode = Dungeon.PRNG.nextInt(nodes.size());
        return nodes.get(randomNode);
    }

    public void randomizeLocation()
    {
        currentNode = generateLocation();
        currentX = currentNode.getX() * DungeonComp.TILE_SIZE;
        currentY = currentNode.getY() * DungeonComp.TILE_SIZE;
        gotoNode = currentNode;
    }

    public Species getSpecies()
    {
        return species;
    }

    public Node getCurrentNode()
    {
        return currentNode;
    }

    public void setCurrentNode(Node n)
    {
        currentNode = n;
        currentX = currentNode.getX() * DungeonComp.TILE_SIZE;
        currentY = currentNode.getY() * DungeonComp.TILE_SIZE;
    }

    public Node getDestinationNode()
    {
        return gotoNode;
    }

    public void setDestinationNode(Node n)
    {
        gotoNode = n;
    }

    public int getX()
    {
        return currentNode.getX();
    }

    public int getY()
    {
        return currentNode.getY();
    }

    public int getPixelX()
    {
        return currentX;
    }

    public int getPixelY()
    {
        return currentY;
    }

    public void setPixel(int newX, int newY)
    {
        currentX = newX;
        currentY = newY;
    }

    public void addPixel(int dx, int dy)
    {
        currentX += dx;
        currentY += dy;
    }

    public void setState(EntityState state)
    {	
        currentState = state;
        if(state.getClass().equals(FollowState.class))
        {
            MysteryDungeon.LOG.append(String.format("%s gave chase!\n", species.getName()));
        }
    }

    public void doState()
    {
        if(currentState != null)
        {
            currentState.doState(this, dungeon);
        }
    }

    public EntityState getState()
    {
        return currentState;
    }

    public boolean isWater()
    {
        return species.isWater();
    }

    public boolean isMoving()
    {
        return moving;
    }

    public void setMoving(boolean set)
    {
        moving = set;
    }

    public int getCurrentHP(){return currentHP;}
    public int getMaximumHP(){return maxHP;}

    public void setCurrentHP(int newval)
    {
        if(newval > maxHP){newval = maxHP;}
        else if(newval < 0){newval = 0;}
        currentHP = newval;
        updateHPBar();
    }

    public void setMaxHP(int newval)
    {
        maxHP = newval;
        updateHPBar();
    }

    public int addHP(int dhp)
    {
        int oldHP = currentHP;
        currentHP += dhp;
        if(currentHP > maxHP){currentHP = maxHP;}
        else if(currentHP < 0){currentHP = 0;}
        updateHPBar();
        return oldHP - currentHP;
    }

    private void updateHPBar()
    {
        if(isPlayer)
        {
            MysteryDungeon.HPBAR.setValue(currentHP);
            MysteryDungeon.HPBAR.setMaximum(maxHP);
            MysteryDungeon.HPBAR.setString(String.format("%d/%d", currentHP, maxHP));
        }
    }

    public void setName(String newname)
    {
        name = newname;
    }

    public String getName()
    {
        return name;
    }

    public ArrayList<Move> getMoves()
    {
        return knownMoves;
    }

    public void addMove(Move newMove)
    {
        knownMoves.add(newMove);
    }

    @Override
    public String toString()
    {
        return String.format("[Entity: %s @ %d, %d]", species.getName(), getX(), getY());
    }
}
