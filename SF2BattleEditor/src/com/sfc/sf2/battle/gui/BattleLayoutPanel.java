/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battle.gui;

import com.sfc.sf2.battle.AIPoint;
import com.sfc.sf2.battle.AIRegion;
import com.sfc.sf2.battle.Ally;
import com.sfc.sf2.battle.Battle;
import com.sfc.sf2.battle.BattleSpriteset;
import com.sfc.sf2.battle.Enemy;
import com.sfc.sf2.battle.actions.SpritesetPosActionData;
import com.sfc.sf2.battle.actions.SpritesetRegionActionData;
import com.sfc.sf2.battle.mapterrain.gui.BattleMapTerrainLayoutPanel;
import com.sfc.sf2.core.actions.ActionManager;
import com.sfc.sf2.core.actions.CustomAction;
import com.sfc.sf2.core.gui.layout.BaseMouseCoordsComponent;
import static com.sfc.sf2.graphics.Block.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Block.PIXEL_WIDTH;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.helpers.GraphicsHelpers;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;

/**
 *
 * @author wiz
 */
public class BattleLayoutPanel extends BattleMapTerrainLayoutPanel {
    
    public enum BattlePaintMode {
        None,
        Terrain,
        Spriteset,
    }
    
    public enum SpritesetPaintMode {
        Ally,
        Enemy,
        AiRegion,
        AiPoint,
    }
    
    public static Color REGION_TRAINGLE_1 = new Color(100, 100, 255, 75);
    public static Color REGION_TRAINGLE_2 = new Color(255, 100, 255, 75);
    
    private Battle battle;
    
    private BattlePaintMode paintMode = BattlePaintMode.None;
    private SpritesetPaintMode spritesetMode = SpritesetPaintMode.Ally;
    private int selectedSpritesetEntity = -1;
    private int closestRegionPoint;
    
    private boolean drawSprites = true;
    private boolean drawAiRegions = false;
    private boolean drawAiPoints = false;
    private Image alertImage;
    
    private ActionListener spritesetEditedListener;
    private List<int[]> actions = new ArrayList<>();

    public BattleLayoutPanel() {
        super();
        mouseInput.setMouseMotionListener(this::onMouseMove);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawAIRegionNode(g);
    }
            
    @Override
    public void drawImage(Graphics graphics) {
        super.drawImage(graphics);
        Graphics2D g2 = (Graphics2D)graphics;
        
        int x = battle.getBattleCoords().getX();
        int y = battle.getBattleCoords().getY();
        drawAllies(g2, x, y);
        drawEnemies(g2, x, y);
        drawAIRegions(g2, x, y);
        drawAIPoints(g2, x, y);
        drawSelected(g2, x, y);
        drawAlerts(g2, x, y);
    }
    
    private void drawAllies(Graphics2D g2, int battleX, int battleY) {
        if (!drawSprites) return;
        Ally[] allies = battle.getSpriteset().getAllies();
        for (int i=0; i < allies.length; i++) {
            Ally ally = allies[i];
            Font font = new Font("SansSerif", Font.BOLD, 16);
            g2.setFont(font);
            int offset = (i+1 < 10) ? 0 : -4;
            int targetX = (battleX+ally.getX())*PIXEL_WIDTH + 8+offset;
            int targetY = (battleY+ally.getY())*PIXEL_HEIGHT + 18;
            String val = String.valueOf(i+1);
            g2.setColor(Color.BLACK);
            g2.drawString(val, targetX-1, targetY-1);
            g2.drawString(val, targetX-1, targetY+1);
            g2.drawString(val, targetX+1, targetY-1);
            g2.drawString(val, targetX+1, targetY+1);
            g2.setColor(Color.YELLOW);
            g2.drawString(val, targetX, targetY);
            if (paintMode == BattlePaintMode.Spriteset && spritesetMode == SpritesetPaintMode.Ally && i == selectedSpritesetEntity) {
                g2.setColor(Color.YELLOW);
                g2.setStroke(new BasicStroke(3));
                g2.drawRect((battleX+ally.getX())*PIXEL_WIDTH, (battleY+ally.getY())*PIXEL_HEIGHT, PIXEL_WIDTH, PIXEL_HEIGHT);
            }
        }
    }
    
    private void drawEnemies(Graphics2D g2, int battleX, int battleY) {
        if (!drawSprites) return;
        Enemy[] enemies = battle.getSpriteset().getEnemies();
        for (int i=0; i < enemies.length; i++) {
            Enemy enemy = enemies[i];
            int targetX = (battleX+enemy.getX())*PIXEL_WIDTH;
            int targetY = (battleY+enemy.getY())*PIXEL_HEIGHT;
            int id = enemy.getEnemyData().getID();
            Tileset sprite = enemy.getEnemyData().getMapSprite();
            if (sprite == null) {
                //Draw number to represent sprite
                targetX += 8 + ((enemy.getEnemyData().getID() + 1 < 10) ? 0 : -4);
                targetY += 3;
                g2.setColor(Color.BLACK);
                g2.drawString(String.valueOf(id), targetX-1, targetY+16-1);
                g2.drawString(String.valueOf(id), targetX-1, targetY+16+1);
                g2.drawString(String.valueOf(id), targetX+1, targetY+16-1);
                g2.drawString(String.valueOf(id), targetX+1, targetY+16+1);
                g2.setColor(Color.RED);
                g2.drawString(String.valueOf(id), targetX, targetY+16);
            } else if (enemy.getEnemyData().isIsSpecialSprite()) {
                //Draw special sprite
                g2.drawImage(sprite.getIndexedColorImage().getSubimage(0, 0, PIXEL_WIDTH*2, PIXEL_HEIGHT*2), targetX-PIXEL_WIDTH/2, targetY-PIXEL_HEIGHT, null);
            } else {
                //Draw regular sprite
                g2.drawImage(sprite.getIndexedColorImage(), targetX, targetY, null);
            }
        }
        for (int i = 0; i < enemies.length; i++) {
            Enemy enemy = enemies[i];
            g2.setColor(Color.WHITE);
            drawAiTarget(g2, battleX, battleY, enemy.getX(), enemy.getY(), enemy.getMoveOrder(), enemy.getMoveOrderTarget());
        }
    }

    private void drawAiTarget(Graphics2D g2, int mapOffsetX, int mapOffsetY, int enemyX, int enemyY, String order, int target) {
        int targetX = -1, targetY = -1;
        switch (order) {
            case "FOLLOW_TARGET":     //Follow target (ally)
                Ally[] allies = battle.getSpriteset().getAllies();
                if (target >= 0 && target < allies.length) {
                    targetX = allies[target].getX();
                    targetY = allies[target].getY();
                }
                break;
            case "FOLLOW_ENEMY":     //Follow enemy
                Enemy[] enemies = battle.getSpriteset().getEnemies();
                if (target >= 0 && target < enemies.length) {
                    targetX = enemies[target].getX();
                    targetY = enemies[target].getY();
                }
                break;
            case "MOVE_TO":
                AIPoint[] points = battle.getSpriteset().getAiPoints();
                if (target >= 0 && target < points.length) {
                    targetX = points[target].getX();
                    targetY = points[target].getY();
                }
                break;
            default:
                break;
        }
        if (targetX >= 0 && targetY >= 0) {
            GraphicsHelpers.drawArrowLine(g2, (mapOffsetX+enemyX)*PIXEL_WIDTH+12, (mapOffsetY+enemyY)*PIXEL_HEIGHT+12, (mapOffsetX+targetX)*PIXEL_WIDTH+12, (mapOffsetY+targetY)*PIXEL_HEIGHT+12);
        }
    }

    private void drawAIRegions(Graphics2D g2, int battleX, int battleY) {
        if (!drawAiRegions) return;
        g2.setStroke(new BasicStroke(3));
        AIRegion[] regions = battle.getSpriteset().getAiRegions();
        for (int i=0; i < regions.length; i++) {
            drawAIRegion(g2, battleX, battleY, regions[i], false, Color.WHITE);
        }
    }
    
    private void drawAIRegion(Graphics2D g2, int battleX, int battleY, AIRegion region, boolean fillArea, Color borderColor) {
        Point[] points = region.getPoints();
        if (fillArea) {
            switch (region.getType()) {
                case 3: //3-points
                    g2.setColor(REGION_TRAINGLE_1);
                    drawRegionArea(g2, battleX, battleY, points[0], points[1], points[2]);
                break;
                case 4: //4-points
                    g2.setColor(REGION_TRAINGLE_1);
                    drawRegionArea(g2, battleX, battleY, points[0], points[1], points[3]);
                    g2.setColor(REGION_TRAINGLE_2);
                    drawRegionArea(g2, battleX, battleY, points[1], points[2], points[3]);
                break;
            }
        }
        g2.setColor(borderColor);
        drawRegionBounds(g2, battleX, battleY, region);
    }
    
    private void drawRegionArea(Graphics2D g2, int battleX, int battleY, Point p1, Point p2, Point p3) {
        Polygon p = new Polygon();
        p.addPoint((battleX+p1.x)*PIXEL_WIDTH+12, (battleY+p1.y)*PIXEL_HEIGHT+12);
        p.addPoint((battleX+p2.x)*PIXEL_WIDTH+12, (battleY+p2.y)*PIXEL_HEIGHT+12);
        p.addPoint((battleX+p3.x)*PIXEL_WIDTH+12, (battleY+p3.y)*PIXEL_HEIGHT+12);
        g2.fillPolygon(p);
    }
    
    private void drawRegionBounds(Graphics2D g2, int battleX, int battleY, AIRegion region) {
        Point[] points = region.getPoints();
        int pointsCount = region.getType();
        for (int i = 0; i < pointsCount; i++) {
            Point s = points[i];
            Point e = points[(i+1)%pointsCount];
            g2.drawLine((s.x+battleX)*PIXEL_WIDTH+12, (s.y+battleY)*PIXEL_HEIGHT+12, (e.x+battleX)*PIXEL_WIDTH+12, (e.y+battleY)*PIXEL_HEIGHT+12);
        }
    }

    private void drawAIPoints(Graphics2D g2, int battleX, int battleY) {
        if (!drawAiPoints) return;
        g2.setStroke(new BasicStroke(3));
        AIPoint[] points = battle.getSpriteset().getAiPoints();
        for (int i = 0; i < points.length; i++) {
            g2.setColor(Color.WHITE);
            drawAIPoint(g2, battleX, battleY, points[i]);
        }
    }
    
    private void drawAIPoint(Graphics2D g2, int battleX, int battleY, AIPoint point) {
            int px = battleX + point.getX();
            int py = battleY + point.getY();
            g2.drawRect(px*PIXEL_WIDTH, py*PIXEL_HEIGHT, PIXEL_WIDTH, PIXEL_HEIGHT);
    }
    
    private void drawSelected(Graphics2D g2, int battleX, int battleY) {
        if (selectedSpritesetEntity == -1 || spritesetMode == SpritesetPaintMode.Ally) return;
        BattleSpriteset spriteset = battle.getSpriteset();
        switch (spritesetMode) {
            case Enemy:
                Enemy enemy = spriteset.getEnemies()[selectedSpritesetEntity];
                g2.setColor(Color.YELLOW);
                g2.setStroke(new BasicStroke(3));
                g2.drawRect((battleX+enemy.getX())*PIXEL_WIDTH, (battleY+enemy.getY())*PIXEL_HEIGHT, PIXEL_WIDTH, PIXEL_HEIGHT);
                g2.setColor(Color.RED);
                drawAiTarget(g2, battleX, battleY, enemy.getX(), enemy.getY(), enemy.getBackupMoveOrder(), enemy.getBackupMoveOrderTarget());
                if (enemy.getBackupMoveOrder().equals("MOVE_TO")) {
                    int target = enemy.getBackupMoveOrderTarget();
                    if (target >= 0 && target < spriteset.getAiPoints().length) {
                        drawAIPoint(g2, battleX, battleY, spriteset.getAiPoints()[target]);
                    }
                }
                g2.setColor(Color.GREEN);
                drawAiTarget(g2, battleX, battleY, enemy.getX(), enemy.getY(), enemy.getMoveOrder(), enemy.getMoveOrderTarget());
                if (enemy.getMoveOrder().equals("MOVE_TO")) {
                    int target = enemy.getMoveOrderTarget();
                    if (target >= 0 && target < spriteset.getAiPoints().length) {
                        drawAIPoint(g2, battleX, battleY, spriteset.getAiPoints()[target]);
                    }
                }
                int triggerRegion = enemy.getTriggerRegion2();
                if (triggerRegion >=0 && triggerRegion < spriteset.getAiRegions().length) {
                    drawAIRegion(g2, battleX, battleY, spriteset.getAiRegions()[triggerRegion], false, Color.RED);
                }
                triggerRegion = enemy.getTriggerRegion1();
                if (triggerRegion >=0 && triggerRegion < spriteset.getAiRegions().length) {
                    drawAIRegion(g2, battleX, battleY, spriteset.getAiRegions()[triggerRegion], false, Color.GREEN);
                }
                break;
            case AiRegion:
                drawAIRegion(g2, battleX, battleY, spriteset.getAiRegions()[selectedSpritesetEntity], true, Color.YELLOW);
                break;
            case AiPoint:
                g2.setColor(Color.YELLOW);
                drawAIPoint(g2, battleX, battleY, spriteset.getAiPoints()[selectedSpritesetEntity]);
                break;
        }   
    }
    
    private void drawAlerts(Graphics2D g2, int battleX, int battleY) {
        //With the feature added to shift the map without shifting Spriteset items, it is possible for them to be out of bounds. This alerts the user if anything is out of bounds
        Ally[] allies = battle.getSpriteset().getAllies();
        for (int i=0; i < allies.length; i++) {
            drawAlertIfOutOfBounds(g2, battleX, battleY, allies[i].getX(), allies[i].getY());
        }
        Enemy[] enemies = battle.getSpriteset().getEnemies();
        for (int i=0; i < enemies.length; i++) {
            drawAlertIfOutOfBounds(g2, battleX, battleY, enemies[i].getX(), enemies[i].getY());
        }
        AIRegion[] regions = battle.getSpriteset().getAiRegions();
        for (int i=0; i < regions.length; i++) {
            Point[] points = regions[i].getPoints();
            for (int p = 0; p < points.length; p++) {
                drawAlertIfOutOfBounds(g2, battleX, battleY, points[p].x, points[p].y);   
            }
        }
        AIPoint[] points = battle.getSpriteset().getAiPoints();
        for (int i = 0; i < points.length; i++) {
            drawAlertIfOutOfBounds(g2, battleX, battleY, points[i].getX(), points[i].getY());
        }
    }
    
    private void drawAlertIfOutOfBounds(Graphics2D g2, int battleX, int battleY, int x, int y) {
        if (x < 0 || x >= battleCoords.getWidth() || y < 0 || y >= battleCoords.getHeight()) {
            g2.drawImage(getAlertImage(), (battleX+x)*PIXEL_WIDTH, (battleY+y)*PIXEL_HEIGHT, null);
        }
    }
    
    private Image getAlertImage() {
        if (alertImage == null) {
            ClassLoader loader = BattleLayoutPanel.class.getClassLoader();
            alertImage = new ImageIcon(loader.getResource("battle/icons/Alert.png")).getImage();
        }
        return alertImage;
    }
    
    private void drawAIRegionNode(Graphics graphics) {
        //Draw a node to show closest node to mouse cursor (when in AI Region edit mode)
        if (spritesetMode != SpritesetPaintMode.AiRegion || selectedSpritesetEntity == -1) return;
        int battleX = battle.getBattleCoords().getX();
        int battleY = battle.getBattleCoords().getY();
        AIRegion region = battle.getSpriteset().getAiRegions()[selectedSpritesetEntity];
        Point point = region.getPoint(closestRegionPoint);
        float scale = this.getRenderScale();
        int nodeX = (battleX+point.x)*PIXEL_WIDTH;
        int nodeY = (battleY+point.y)*PIXEL_HEIGHT;
        graphics.setColor(Color.CYAN);
        graphics.fillArc((int)((nodeX+12)*scale), (int)((nodeY+12)*scale), (int)(16*scale), (int)(16*scale), 0, 360);
        graphics.setColor(Color.BLUE);
        graphics.fillArc((int)((nodeX+14)*scale), (int)((nodeY+14)*scale), (int)(12*scale), (int)(12*scale), 0, 360);
    }

    public void setSpritesetEditedListener(ActionListener spritesetEditedListener) {
        this.spritesetEditedListener = spritesetEditedListener;
    }

    public Battle getBattle() {
        return battle;
    }

    public void setBattle(Battle battle) {
        this.battle = battle;
        selectedSpritesetEntity = -1;
        redraw();
    }

    public void setDrawSprites(boolean drawSprites) {
        if (this.drawSprites != drawSprites) {
            this.drawSprites = drawSprites;
            redraw();
        }
    }

    public void setDrawAiRegions(boolean drawAiRegions) {
        if (this.drawAiRegions != drawAiRegions) {
            this.drawAiRegions = drawAiRegions;
            redraw();
        }
    }

    public void setDrawAiPoints(boolean drawAiPoints) {
        if (this.drawAiPoints != drawAiPoints) {
            this.drawAiPoints = drawAiPoints;
            redraw();
        }
    }

    public BattlePaintMode getPaintMode() {
        return paintMode;
    }

    public void setPaintMode(BattlePaintMode battlePaintMode) {
        if (this.paintMode != battlePaintMode) {
            this.paintMode = battlePaintMode;
            redraw();
        }
    }

    public SpritesetPaintMode getSpritesetMode() {
        return spritesetMode;
    }

    public void setSpritesetMode(SpritesetPaintMode spritesetPaintMode) {
        if (this.spritesetMode != spritesetPaintMode) {
            this.setPaintMode(BattlePaintMode.Spriteset);
            this.spritesetMode = spritesetPaintMode;
            redraw();
        }
    }

    public int getSelectedAlly() {
        return selectedSpritesetEntity;
    }

    public void setSelectedAlly(int selectedAlly) {
        if (this.selectedSpritesetEntity != selectedAlly) {
            this.selectedSpritesetEntity = selectedAlly;
            redraw();
        }
    }

    public int getSelectedEnemy() {
        return selectedSpritesetEntity;
    }

    public void setSelectedEnemy(int selectedEnemy) {
        setSelectedAlly(selectedEnemy);
    }

    public int getSelectedAIRegion() {
        return selectedSpritesetEntity;
    }

    public void setSelectedAIRegion(int selectedRegion) {
        setSelectedAlly(selectedRegion);
    }

    public int getSelectedAIPoint() {
        return selectedSpritesetEntity;
    }

    public void setSelectedAIPoint(int selectedPoit) {
        setSelectedAlly(selectedPoit);
    }

    public List<int[]> getActions() {
        return actions;
    }

    public void setActions(List<int[]> actions) {
        this.actions = actions;
    }
    
    private void onMouseMove(BaseMouseCoordsComponent.GridMouseMoveEvent evt) {
        if (spritesetMode == SpritesetPaintMode.AiRegion && selectedSpritesetEntity >= 0) {
            int x = evt.x() - battle.getBattleCoords().getX();
            int y = evt.y() - battle.getBattleCoords().getY();
            int region = findClosestRegionPoint(battle.getSpriteset().getAiRegions()[selectedSpritesetEntity], x, y);
            if (closestRegionPoint != region) {
                closestRegionPoint = region;
                this.repaint();
            }
        } 
    }
    
    @Override
    protected void onMouseButtonInput(BaseMouseCoordsComponent.GridMousePressedEvent evt) {
        if (paintMode == BattlePaintMode.None) return;
        else if (paintMode == BattlePaintMode.Terrain) {
            if (isDrawTerrain()) {
                super.onMouseButtonInput(evt);
            }
            return;
        }
        if (selectedSpritesetEntity == -1) return;
        
        //Edit spritesets
        int x = evt.x() - battle.getBattleCoords().getX();
        int y = evt.y() - battle.getBattleCoords().getY();
        if (evt.mouseButton() == MouseEvent.BUTTON1) {
            switch (spritesetMode) {
                case Ally:
                {
                    Ally ally = battle.getSpriteset().getAllies()[selectedSpritesetEntity];
                    Point point = new Point(x, y);
                    if (!ally.getPos().equals(point)) {
                        SpritesetPosActionData newValue = new SpritesetPosActionData(ally, point);
                        SpritesetPosActionData oldValue = new SpritesetPosActionData(ally, ally.getPos());
                        ActionManager.setAndExecuteAction(new CustomAction<SpritesetPosActionData>(this, "Move Ally", this::actionAllyPosChanged, newValue, oldValue));
                    }
                    break;
                }
                case Enemy:
                {
                    Enemy enemy = battle.getSpriteset().getEnemies()[selectedSpritesetEntity];
                    Point point = new Point(x, y);
                    if (!enemy.getPos().equals(point)) {
                        SpritesetPosActionData newValue = new SpritesetPosActionData(enemy, point);
                        SpritesetPosActionData oldValue = new SpritesetPosActionData(enemy, enemy.getPos());
                        ActionManager.setAndExecuteAction(new CustomAction<SpritesetPosActionData>(this, "Move Enemy", this::actionEnemyPosChanged, newValue, oldValue));
                    }
                    break;
                }
                case AiRegion:
                {
                    AIRegion region = battle.getSpriteset().getAiRegions()[selectedSpritesetEntity];
                    if (evt.pressed()) {
                        closestRegionPoint = findClosestRegionPoint(region, x, y);
                    }
                    Point point = new Point(x, y);
                    if (!region.getPoint(closestRegionPoint).equals(point)) {
                        SpritesetRegionActionData newValue = new SpritesetRegionActionData(region, closestRegionPoint, point);
                        SpritesetRegionActionData oldValue = new SpritesetRegionActionData(region, closestRegionPoint, region.getPoint(closestRegionPoint));
                        ActionManager.setAndExecuteAction(new CustomAction<SpritesetRegionActionData>(this, "Move AI Region", this::actionAIRegionPosChanged, newValue, oldValue));
                    }
                }
                    break;
                case AiPoint:
                {
                    AIPoint aiPoint = battle.getSpriteset().getAiPoints()[selectedSpritesetEntity];
                    Point point = new Point(x, y);
                    if (!aiPoint.getPos().equals(point)) {
                        SpritesetPosActionData newValue = new SpritesetPosActionData(aiPoint, point);
                        SpritesetPosActionData oldValue = new SpritesetPosActionData(aiPoint, aiPoint.getPos());
                        ActionManager.setAndExecuteAction(new CustomAction<SpritesetPosActionData>(this, "Move AI Point", this::actionAIPointPosChanged, newValue, oldValue));
                    }
                    break;
                }
            }
        }
    }
    
    private void actionAllyPosChanged(SpritesetPosActionData value) {
        Ally ally = (Ally)value.item();
        ally.setPos(value.pos());
        redraw();
        if (spritesetEditedListener != null) {
            spritesetEditedListener.actionPerformed(new ActionEvent(this, selectedSpritesetEntity, "Ally"));
        }
    }
    
    private void actionEnemyPosChanged(SpritesetPosActionData value) {
        Enemy enemy = (Enemy)value.item();
        enemy.setPos(value.pos());
        if (spritesetEditedListener != null) {
            spritesetEditedListener.actionPerformed(new ActionEvent(this, selectedSpritesetEntity, "Enemy"));
        }
    }
    
    private void actionAIRegionPosChanged(SpritesetRegionActionData value) {
        value.region().setPoint(value.regionPoint(), value.pos());
        if (spritesetEditedListener != null) {
            spritesetEditedListener.actionPerformed(new ActionEvent(this, selectedSpritesetEntity, "AiRegion"));
        }
    }
    
    private void actionAIPointPosChanged(SpritesetPosActionData value) {
        AIPoint point = (AIPoint)value.item();
        point.setPos(value.pos());
        if (spritesetEditedListener != null) {
            spritesetEditedListener.actionPerformed(new ActionEvent(this, selectedSpritesetEntity, "AiPoint"));
        }
    }
    
    private int findClosestRegionPoint(AIRegion region, int x, int y) {
        int closest = -1;
        double distance = Integer.MAX_VALUE;
        Point mouse = new Point(x, y);
        Point[] points = region.getPoints();
        int pointCount = region.getType();
        for (int i = 0; i < pointCount; i++) {
            double dist = mouse.distanceSq(points[i]);
            if (dist < distance) {
                closest = i;
                distance = dist;
            }
        }
        return closest;
    }
}
