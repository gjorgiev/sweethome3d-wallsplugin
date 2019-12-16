package com.eteks.plugins;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.eteks.sweethome3d.model.DimensionLine;
import com.eteks.sweethome3d.model.Label;
import com.eteks.sweethome3d.model.TextStyle;
import com.eteks.sweethome3d.model.Wall;
import com.eteks.sweethome3d.plugin.Plugin;
import com.eteks.sweethome3d.plugin.PluginAction;

public class WallsPlugin extends Plugin {

	protected void drawRoom(float A, float B, float C1, float C2, String unit, String title, String footnote, String floatingtext) {
		final float THICKNESS = 0.1f;
		final float HEIGHT = 1f;
		final float FONT_SIZE_FACTOR_TITLE = 0.06f;
		final float FONT_SIZE_FACTOR_FOOTNOTE = 0.05f;
		final float FONT_SIZE_FACTOR_FLOATING = 0.05f;
		final float FONT_SIZE_FACTOR_DIMENSION = 0.05f;
		final float TITLE_DISTANCE_CONST = 3.5f;
		final float FLOATING_TEXT_DISTANCE_CONST = 4f;
		final float FOOTNOTE_DISTANCE_CONST = 0.02f;
		final float DIMENSION_DISTANCE_CONST = 1.5f;
		switch(unit) {
		case "mm":
			A = A / 10;
			B = B /10;
			C1 = C1 / 10;
			C2 = C2 / 10;
			break;
		case "metres":
			A = A * 100;
			B = B * 100;
			C1 = C1 * 100;
			C2 = C2 * 100;
			break;
		default:
			break;
		}
		Wall wallA = new Wall(0,0, A, 0, THICKNESS, HEIGHT);
		Wall wallB1 = new Wall(0,0,0,B,THICKNESS,HEIGHT);
		Wall wallB2 = new Wall(A,0,A,B,THICKNESS,HEIGHT);
		Wall wallC1 = new Wall(0,B,C1,B,THICKNESS,HEIGHT);
		Wall wallC2 = new Wall(A-C2,B,A,B,THICKNESS,HEIGHT);
		final float fontSizeDimension = A * FONT_SIZE_FACTOR_DIMENSION;
		final float dimensionOffset = DIMENSION_DISTANCE_CONST*fontSizeDimension;
		DimensionLine dimensionA = new DimensionLine(0,0,A,0,-dimensionOffset);
		DimensionLine dimensionB1 = new DimensionLine(0,0,0,B,dimensionOffset);
		DimensionLine dimensionB2 = new DimensionLine(A,0,A,B,-dimensionOffset);
		DimensionLine dimensionC1 = new DimensionLine(0,B,C1,B,dimensionOffset);
		DimensionLine dimensionC2 = new DimensionLine(A-C2,B,A,B,dimensionOffset);
		TextStyle styleDimension = new TextStyle(fontSizeDimension);
		dimensionA.setLengthStyle(styleDimension);
		dimensionB1.setLengthStyle(styleDimension);
		dimensionB2.setLengthStyle(styleDimension);
		dimensionC1.setLengthStyle(styleDimension);
		dimensionC2.setLengthStyle(styleDimension);
		final float titleFontSize = A * FONT_SIZE_FACTOR_TITLE;
		final float footnoteFontSize = A * FONT_SIZE_FACTOR_FOOTNOTE;
		final float floatingTextFontSize = A * FONT_SIZE_FACTOR_FLOATING;
		final float titleDistanceY = titleFontSize * TITLE_DISTANCE_CONST;
		final float footnoteDistanceY = B/2 - B * FOOTNOTE_DISTANCE_CONST;
		final float floatingTextDistanceY = B + floatingTextFontSize * FLOATING_TEXT_DISTANCE_CONST;
		Label titleLabel = new Label(title, A/2, -titleDistanceY);
		Label footnoteLabel = new Label(footnote, A/2, footnoteDistanceY);
		Label floatingtextLabel = new Label(floatingtext, A/2, floatingTextDistanceY);
		TextStyle styleTitle = new TextStyle(titleFontSize);
		TextStyle styleFootnote = new TextStyle(footnoteFontSize);
		TextStyle styleFloating = new TextStyle(floatingTextFontSize);
		titleLabel.setStyle(styleTitle);
		footnoteLabel.setStyle(styleFootnote);
		floatingtextLabel.setStyle(styleFloating);
		getHome().addWall(wallA);
		getHome().addWall(wallB1);
		getHome().addWall(wallB2);
		getHome().addWall(wallC1);
		getHome().addWall(wallC2);
		getHome().addDimensionLine(dimensionA);
		getHome().addDimensionLine(dimensionB1);
		getHome().addDimensionLine(dimensionB2);
		getHome().addDimensionLine(dimensionC1);
		getHome().addDimensionLine(dimensionC2);
		getHome().addLabel(titleLabel);
		getHome().addLabel(footnoteLabel);
		getHome().addLabel(floatingtextLabel);
	}
	
	protected void showWindowDialog() {
		JDialog dialog = new JDialog();
		JPanel fieldsPnl = new JPanel();
		fieldsPnl.setLayout(new BoxLayout(fieldsPnl, BoxLayout.Y_AXIS));
		fieldsPnl.setBorder(BorderFactory.createEmptyBorder(10,10,10,5));
		dialog.setLayout(new BorderLayout());
		dialog.setTitle("Create room");
		final int TEXT_FIELD_SIZE = 15;

		
		JLabel titleLbl = new JLabel("Please enter the room dimensions");
		JPanel titlePnl = new JPanel();
		titlePnl.setBorder(BorderFactory.createEmptyBorder(10,10,0,10));
		titlePnl.add(titleLbl);
		dialog.add(titlePnl, BorderLayout.PAGE_START);
		
		JLabel labelA = new JLabel("A", JLabel.LEFT);
		JTextField textFieldA = new JTextField(TEXT_FIELD_SIZE);
		JPanel panelA = new JPanel();
		panelA.add(labelA);
		panelA.add(textFieldA);
		fieldsPnl.add(panelA);
		
		JLabel labelB = new JLabel("B");
		JTextField textFieldB = new JTextField(TEXT_FIELD_SIZE);
		JPanel panelB = new JPanel();
		panelB.add(labelB);
		panelB.add(textFieldB);
		fieldsPnl.add(panelB);
		
		JLabel labelC1 = new JLabel("C1");
		JTextField textFieldC1 = new JTextField(TEXT_FIELD_SIZE);
		JPanel panelC1 = new JPanel();
		panelC1.add(labelC1);
		panelC1.add(textFieldC1);
		fieldsPnl.add(panelC1);
		
		JLabel labelC2 = new JLabel("C2");
		JTextField textFieldC2 = new JTextField(TEXT_FIELD_SIZE);
		JPanel panelC2 = new JPanel();
		panelC2.add(labelC2);
		panelC2.add(textFieldC2);
		fieldsPnl.add(panelC2);
		
		JLabel labelTitle = new JLabel("Title");
		JTextField textFieldTitle = new JTextField(TEXT_FIELD_SIZE);
		JPanel panelTitle = new JPanel();
		panelTitle.add(labelTitle);
		panelTitle.add(textFieldTitle);
		fieldsPnl.add(panelTitle);
		
		JLabel labelFootnote = new JLabel("Footnote");
		JTextField textFieldFootnote = new JTextField(TEXT_FIELD_SIZE);
		JPanel panelFootnote = new JPanel();
		panelFootnote.add(labelFootnote);
		panelFootnote.add(textFieldFootnote);
		fieldsPnl.add(panelFootnote);
		
		JLabel labelFloatingtext = new JLabel("Floating text");
		JTextField textFieldFloatingtext = new JTextField(TEXT_FIELD_SIZE);
		JPanel panelFloatingtext = new JPanel();
		panelFloatingtext.add(labelFloatingtext);
		panelFloatingtext.add(textFieldFloatingtext);
		fieldsPnl.add(panelFloatingtext);
		
		JPanel unitsPnl = new JPanel();
		unitsPnl.setLayout(new BoxLayout(unitsPnl, BoxLayout.Y_AXIS));
		JLabel unitsLbl = new JLabel("Dimension unit");
		JRadioButton mmOpt = new JRadioButton("mm");
		JRadioButton cmOpt = new JRadioButton("cm");
		JRadioButton metresOpt = new JRadioButton("Metres");
		ButtonGroup group = new ButtonGroup();
		cmOpt.setSelected(true);
		mmOpt.setActionCommand("mm");
		cmOpt.setActionCommand("cm");
		metresOpt.setActionCommand("metres");
		group.add(mmOpt);
		group.add(cmOpt);
		group.add(metresOpt);
		unitsPnl.add(unitsLbl);
		unitsPnl.add(mmOpt);
		unitsPnl.add(cmOpt);
		unitsPnl.add(metresOpt);
		unitsPnl.setBorder(BorderFactory.createEmptyBorder(10,5,10,10));
		
		JButton createRoomBtn = new JButton("Create room");
		createRoomBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					float A = Float.parseFloat(textFieldA.getText());
					float B = Float.parseFloat(textFieldB.getText());
					float C1 = Float.parseFloat(textFieldC1.getText());
					float C2 = Float.parseFloat(textFieldC2.getText());
					String title = textFieldTitle.getText();
					String footnote = textFieldFootnote.getText();
					String floatingtext = textFieldFloatingtext.getText();
					String option = group.getSelection().getActionCommand();
					drawRoom(A, B, C1, C2, option, title, footnote, floatingtext);
					dialog.dispose();
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage(), 
							"Please insert only numbers", JOptionPane.ERROR_MESSAGE);
				}
				
				
				
			}
		});
		JPanel createRoomPanelBtn = new JPanel();
		createRoomPanelBtn.add(createRoomBtn);
		fieldsPnl.add(createRoomPanelBtn);
		dialog.add(fieldsPnl, BorderLayout.LINE_START);
		dialog.add(unitsPnl, BorderLayout.LINE_END);
		dialog.setSize(350,400);
		dialog.setLocationRelativeTo(null);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.pack();
		dialog.setVisible(true);
	}

	public class WallsAction extends PluginAction {

		public WallsAction () {
			putPropertyValue(Property.NAME, "Create Room");
			putPropertyValue(Property.MENU, "Tools");
			setEnabled(true);
		}
		
		@Override
		public void execute() {		
			showWindowDialog();
		}

	}

	@Override
	public PluginAction[] getActions() {
		// TODO Auto-generated method stub
		return new PluginAction [] {new WallsAction()};
	}

}
