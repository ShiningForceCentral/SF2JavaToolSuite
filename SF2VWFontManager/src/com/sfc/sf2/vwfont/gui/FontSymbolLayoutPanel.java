/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.vwfont.gui;

import com.sfc.sf2.core.gui.AbstractLayoutPanel;
import com.sfc.sf2.core.gui.layout.*;
import com.sfc.sf2.vwfont.FontSymbol;
import static com.sfc.sf2.vwfont.FontSymbol.PIXEL_HEIGHT;
import static com.sfc.sf2.vwfont.FontSymbol.PIXEL_WIDTH;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 *
 * @author wiz
 */
public class FontSymbolLayoutPanel extends AbstractLayoutPanel {
    
    private static final int DEFAULT_SYMBOLS_PER_ROW = 10;
        
    private FontSymbol[] symbols;   //This panel simulates the secret empty symbol 0 (i+1)
    
    public FontSymbolLayoutPanel() {
        super();
        background = new LayoutBackground(Color.LIGHT_GRAY);
        scale = new LayoutScale(1);
        grid = new LayoutGrid(PIXEL_WIDTH, PIXEL_HEIGHT);
        coordsGrid = new LayoutCoordsGridDisplay(PIXEL_WIDTH, PIXEL_HEIGHT, true);
        coordsHeader = new LayoutCoordsHeader(this, PIXEL_WIDTH, PIXEL_HEIGHT, true);
        mouseInput = null;
        scroller = new LayoutScrollNormaliser(this);
        setItemsPerRow(DEFAULT_SYMBOLS_PER_ROW);
    }

    @Override
    protected boolean hasData() {
        return symbols != null && symbols.length > 0;
    }
    
    @Override
    protected Dimension getImageDimensions() {
        int symbolsPerRow = getItemsPerRow();
        int length = symbols.length+1;
        int w = symbolsPerRow * PIXEL_WIDTH;
        int h = (length/symbolsPerRow) * PIXEL_HEIGHT;
        if (length%symbolsPerRow != 0) {
            h += PIXEL_HEIGHT;
        }
        return new Dimension(w, h);
    }

    @Override
    protected void drawImage(Graphics graphics) {
        int symbolsPerRow = getItemsPerRow();
        if (isShowGrid()) {
            graphics.setColor(background.getBgColor().darker());
            for (int i = 0; i < symbols.length; i++) {
                int x = symbols[i].getWidth() + ((i+1)%symbolsPerRow)*PIXEL_WIDTH;
                int y = ((i+1)/symbolsPerRow)*PIXEL_HEIGHT;
                graphics.drawLine(x, y, x, y+PIXEL_HEIGHT-1);
            }
        }
        for (int i = 0; i < symbols.length; i++) {
            graphics.drawImage(symbols[i].getIndexColoredImage(), ((i+1)%symbolsPerRow)*PIXEL_WIDTH, ((i+1)/symbolsPerRow)*PIXEL_HEIGHT, null);
        }
    }

    public FontSymbol[] getFontSymbols() {
        return symbols;
    }

    public void setFontSymbols(FontSymbol[] symbols) {
        this.symbols = symbols;
        redraw();
    }
}
