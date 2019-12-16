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
		final float FLOATING_TEXT_DISTANCE_CONST = 5f;
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
		final float footnoteDistanceY = B/2;
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
		fieldsPnl.setBorder(BorderFactory.createEmptyBorder(10,10,0,10));
		dialog.setLayout(new BorderLayout());
		dialog.setTitle("Create room");
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		final int TEXT_FIELD_SIZE = 15;

		
		JLabel titleLbl = new JLabel("Please enter the room dimensions");
		titleLbl.setBorder(BorderFactory.createEmptyBorder(10,10,0,10));
		dialog.add(titleLbl, BorderLayout.PAGE_START);
		
		JLabel labelA = new JLabel("A", JLabel.LEFT);
		labelA.setBorder(BorderFactory.createEmptyBorder(0,0,0,5));
		JTextField textFieldA = new JTextField(TEXT_FIELD_SIZE);
		JPanel panelA = new JPanel();
		panelA.setLayout(new BorderLayout());
		panelA.setBorder(BorderFactory.createEmptyBorder(0,0,5,0));
		panelA.add(labelA, BorderLayout.LINE_START);
		panelA.add(textFieldA, BorderLayout.LINE_END);
		fieldsPnl.add(panelA);
		
		JLabel labelB = new JLabel("B");
		labelB.setBorder(BorderFactory.createEmptyBorder(0,0,0,5));
		JTextField textFieldB = new JTextField(TEXT_FIELD_SIZE);
		JPanel panelB = new JPanel();
		panelB.setLayout(new BorderLayout());
		panelB.setBorder(BorderFactory.createEmptyBorder(0,0,5,0));
		panelB.add(labelB, BorderLayout.LINE_START);
		panelB.add(textFieldB, BorderLayout.LINE_END);
		fieldsPnl.add(panelB);
		
		JLabel labelC1 = new JLabel("C1");
		labelC1.setBorder(BorderFactory.createEmptyBorder(0,0,0,5));
		JTextField textFieldC1 = new JTextField(TEXT_FIELD_SIZE);
		JPanel panelC1 = new JPanel();
		panelC1.setLayout(new BorderLayout());
		panelC1.setBorder(BorderFactory.createEmptyBorder(0,0,5,0));
		panelC1.add(labelC1, BorderLayout.LINE_START);
		panelC1.add(textFieldC1, BorderLayout.LINE_END);
		fieldsPnl.add(panelC1);
		
		JLabel labelC2 = new JLabel("C2");
		labelC2.setBorder(BorderFactory.createEmptyBorder(0,0,0,5));
		JTextField textFieldC2 = new JTextField(TEXT_FIELD_SIZE);
		JPanel panelC2 = new JPanel();
		panelC2.setLayout(new BorderLayout());
		panelC2.setBorder(BorderFactory.createEmptyBorder(0,0,5,0));
		panelC2.add(labelC2, BorderLayout.LINE_START);
		panelC2.add(textFieldC2, BorderLayout.LINE_END);
		fieldsPnl.add(panelC2);
		
		JLabel labelTitle = new JLabel("Title");
		labelTitle.setBorder(BorderFactory.createEmptyBorder(0,0,0,5));
		JTextField textFieldTitle = new JTextField(TEXT_FIELD_SIZE);
		JPanel titlePnl = new JPanel();
		titlePnl.setLayout(new BorderLayout());
		titlePnl.setBorder(BorderFactory.createEmptyBorder(0,0,5,0));
		titlePnl.add(labelTitle, BorderLayout.LINE_START);
		titlePnl.add(textFieldTitle, BorderLayout.LINE_END);
		fieldsPnl.add(titlePnl);
		
		JLabel labelFootnote = new JLabel("Footnote");
		labelFootnote.setBorder(BorderFactory.createEmptyBorder(0,0,0,5));
		JTextField textFieldFootnote = new JTextField(TEXT_FIELD_SIZE);
		JPanel footnotePnl = new JPanel();
		footnotePnl.setLayout(new BorderLayout());
		footnotePnl.setBorder(BorderFactory.createEmptyBorder(0,0,5,0));
		footnotePnl.add(labelFootnote, BorderLayout.LINE_START);
		footnotePnl.add(textFieldFootnote, BorderLayout.LINE_END);
		fieldsPnl.add(footnotePnl);
		
		JLabel labelFloatingtext = new JLabel("Floating text");
		labelFloatingtext.setBorder(BorderFactory.createEmptyBorder(0,0,0,5));
		JTextField textFieldFloatingtext = new JTextField(TEXT_FIELD_SIZE);
		JPanel floatingTextPnl = new JPanel();
		floatingTextPnl.setLayout(new BorderLayout());
		floatingTextPnl.setBorder(BorderFactory.createEmptyBorder(0,0,5,0));
		floatingTextPnl.add(labelFloatingtext, BorderLayout.LINE_START);
		floatingTextPnl.add(textFieldFloatingtext, BorderLayout.LINE_END);
		fieldsPnl.add(floatingTextPnl);
		
		JPanel unitsPnl = new JPanel();
		unitsPnl.setLayout(new BoxLayout(unitsPnl, BoxLayout.Y_AXIS));
		unitsPnl.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		JLabel unitsLbl = new JLabel("Dimension Unit");
		JRadioButton mmOpt = new JRadioButton("mm");
		JRadioButton cmOpt = new JRadioButton("cm");
		JRadioButton metresOpt = new JRadioButton("Metres");
		ButtonGroup group = new ButtonGroup();
		cmOpt.setSelected(true);
		mmOpt.setActionCommand("mm");
		mmOpt.setBorder(BorderFactory.createEmptyBorder(10,0,5,0));
		cmOpt.setActionCommand("cm");
		cmOpt.setBorder(BorderFactory.createEmptyBorder(5,0,5,0));
		metresOpt.setActionCommand("metres");
		metresOpt.setBorder(BorderFactory.createEmptyBorder(5,0,5,0));
		group.add(mmOpt);
		group.add(cmOpt);
		group.add(metresOpt);
		unitsPnl.add(unitsLbl);
		unitsPnl.add(mmOpt);
		unitsPnl.add(cmOpt);
		unitsPnl.add(metresOpt);
		
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
		
		JPanel buttonPnl = new JPanel(new BorderLayout());
		buttonPnl.setBorder(BorderFactory.createEmptyBorder(5,0,10,0));
		buttonPnl.add(createRoomBtn);
		fieldsPnl.add(buttonPnl);
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
