/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.item;

import java.awt.Component;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;


/**
 * Used to render the item list in a nicer fashion.
 * Rather than a simple list of items, written in text form,
 * I'd prefer to draw a small image next to them as well. This renderer
 * can do such a thing.
 * @author Justis
 */
public class ItemComboBoxRenderer extends JLabel implements ListCellRenderer
{

    /**
     *
     */
    public ItemComboBoxRenderer()
    {
        setHorizontalAlignment(LEFT);
        setVerticalAlignment(CENTER);
        setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
    {
        if(isSelected)
        {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        }
        else
        {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        if(value != null)
        {
            Item item = (Item)value;
            setIcon(new ImageIcon(item.getImage()));
            setText(item.getName());
            setToolTipText(item.getDescription());
            setFont(new Font(Font.MONOSPACED, Font.PLAIN, 18));
        }
        return this;
    }
    
    
    
    
}
