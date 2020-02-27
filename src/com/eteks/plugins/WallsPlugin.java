package com.eteks.plugins;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.eteks.sweethome3d.model.DimensionLine;
import com.eteks.sweethome3d.model.HomePieceOfFurniture;
import com.eteks.sweethome3d.model.Label;
import com.eteks.sweethome3d.model.PieceOfFurniture;
import com.eteks.sweethome3d.model.TextStyle;
import com.eteks.sweethome3d.model.Wall;
import com.eteks.sweethome3d.plugin.Plugin;
import com.eteks.sweethome3d.plugin.PluginAction;


public class WallsPlugin extends Plugin {
	
	final float HEIGHT = 0.1f;
	
	private float normalize(float value, String unit)
	{
		switch(unit) {
		case "mm":
			return value / 10;
		case "metres":
			return value * 100;
		default:
			return value;
		}
	}

	protected void drawRoom(float A, float B, float C1, float C2, String unit, String title, String footnote, String floatingtext, float footnoteTextLines) {
		final float THICKNESS = 0.1f;
		final float TITLE_DISTANCE_CONST = 3f;
		final float FOOTNOTE_DISTANCE_CONST = 2f;
		final float DIMENSION_DISTANCE_CONST = 1.5f;
		
		// Convert all values to centimeters
		A = normalize(A, unit);
		B = normalize(B, unit);
		C1 = normalize(C1, unit);
		C2 = normalize(C2, unit);
		
		Wall wallA = new Wall(0,0, A, 0, THICKNESS, HEIGHT);
		Wall wallB1 = new Wall(0,0,0,B,THICKNESS,HEIGHT);
		Wall wallB2 = new Wall(A,0,A,B,THICKNESS,HEIGHT);
		Wall wallC1 = new Wall(0,B,C1,B,THICKNESS,HEIGHT);
		Wall wallC2 = new Wall(A-C2,B,A,B,THICKNESS,HEIGHT);
		
		
		final float dimensionOffset = DIMENSION_DISTANCE_CONST*A*0.05f;
		DimensionLine dimensionA = new DimensionLine(0,0,A,0,-dimensionOffset);
		DimensionLine dimensionB1 = new DimensionLine(0,0,0,B,dimensionOffset);
		DimensionLine dimensionB2 = new DimensionLine(A,0,A,B,-dimensionOffset);
		DimensionLine dimensionC1 = new DimensionLine(0,B,C1,B,dimensionOffset);
		DimensionLine dimensionC2 = new DimensionLine(A-C2,B,A,B,dimensionOffset);
		
		final float titleFontSize = 18;
		final float footnoteFontSize = 11;
		final float floatingTextFontSize = 8;
		final float titleDistanceY = titleFontSize * TITLE_DISTANCE_CONST;
		float footnoteDistanceY = B + footnoteFontSize * (FOOTNOTE_DISTANCE_CONST + footnoteTextLines);
		footnoteDistanceY += (C1 > 0 || C2 > 0 ? 1.5*dimensionOffset : 0);
		final float floatingTextDistanceY = B/2;

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
		if (C1 > 0)
			getHome().addDimensionLine(dimensionC1);
		if (C2 > 0)
			getHome().addDimensionLine(dimensionC2);
		
		getHome().addLabel(titleLabel);
		getHome().addLabel(footnoteLabel);
		getHome().addLabel(floatingtextLabel);
	}

	
	private void drawBox(float X, float Y, float width, float depth)
	{
		PieceOfFurniture pof = getUserPreferences().getFurnitureCatalog().getCategory(6).getPieceOfFurniture(0);
		HomePieceOfFurniture box = new HomePieceOfFurniture(pof);
		box.setHeight(HEIGHT);
		getHome().addPieceOfFurniture(box);
		box.setX(X);
		box.setY(Y);
		box.setWidth(width);
		box.setDepth(depth);
	}
	
	private float calculateShortSide(float C)
	{
		if (C >= 30 && C < 45)
			return 30f;
		else if (C >= 45 && C < 60)
			return 45f;
		else if(C >= 60)
			return 60f;
		else
		{
			System.out.println("Cannot draw boxes with C = " + C + System.lineSeparator() + "Please enter values greater than 30 for C1 or C2");
			return 0f;
		}
	}
	
	private void drawRacksB(float C1, float C2, Combination combination, float A)
	{
		float shortSide;
		float X;
		if (C1 >= C2) {
			shortSide = calculateShortSide(C1);
			X = shortSide / 2;
		}
		else {
			shortSide = calculateShortSide(C2);
			X = A - shortSide / 2;
		}
		List<Float> longSides = combination.getCombination();
		// Initialize the Y position for first box
		float Y = longSides.get(0) / 2;
		for (int i = 0; i < longSides.size(); i++) {
			float longSide = longSides.get(i);
			if (i > 0)
				Y += longSides.get(i-1) / 2 + longSide / 2;
			drawBox(X, Y, shortSide, longSide);
		}
	}
	
	private void drawRacksA(float C1, float C2, Combination combination, float A)
	{
		List<Float> longSides = combination.getCombination();
		float shortSide;
		float X;
		if (C1 >= C2) {
			shortSide = calculateShortSide(C1);
			X = longSides.get(0) / 2 + shortSide;
		}
		else {
			shortSide = calculateShortSide(C2);
			X = A - (shortSide + longSides.get(0) / 2);
		}
		float Y = shortSide / 2;

		for (int i = 0; i < longSides.size(); i++) {
			float longSide = longSides.get(i);
			if (i > 0)
				if (C1 >= C2) {
					X += longSides.get(i-1) / 2 + longSide / 2;
				} else {
					X -= longSides.get(i-1) / 2 + longSide / 2;
				}
			drawBox(X, Y, longSide, shortSide);
		}
	}
	
	private ArrayList<Combination> combinations(List<Float> longSides, float target)
	{
		// lower the target until we get value divisible with 30
        while(target % 30 > 0) {
        	target--;
        	System.out.println("target=" + target);
        }
        Combinations combinations = new Combinations();
        combinations.sum_up(longSides,target);
        return combinations.getCombinations();
	}
	
	private int minSize(ArrayList<Combination> combinations)
	{
		int min = combinations.get(0).getCombination().size();
		for (Combination combination : combinations) {
			if (combination.getCombination().size() < min)
				min = combination.getCombination().size();
		}
		return min;
	}
	
	private float minDiff(ArrayList<Combination> combinations)
	{
		float min = combinations.get(0).getDiff();
		for (Combination combination : combinations) {
			if (combination.getDiff() < min)
				min = combination.getDiff();
		}
		return min;
	}
	
	private Combination bestCombination(ArrayList<Combination> combinations)
	{
		int minSize = minSize(combinations);
		combinations.removeIf(combination -> combination.getCombination().size() > minSize);
		float minDiff = minDiff(combinations);
		System.out.println("minDiff=" + minDiff + " minSize=" + minSize);
		combinations.removeIf(combination -> combination.getDiff() > minDiff);
		if (combinations.isEmpty())
			return null;
		else if (combinations.size() > 1)
		{
			System.out.println("There are more than one best combinations: ");
			for (Combination combination : combinations) {
				System.out.println(combination.toString());
			}
			return null;
		}
		else
			return combinations.get(0);
	}
	
	private void addDimensionLinesRacks(float C1, float C2, Combination bestA, Combination bestB, float A, float B, float shortSide)
	{
		// Double the offset of other dimension lines to make space for the new
		List<DimensionLine> dimension_lines = new ArrayList<DimensionLine>(getHome().getDimensionLines());
		DimensionLine dimensionA = dimension_lines.get(0);
		DimensionLine dimensionB = (C1 >= C2) ? dimension_lines.get(1) : dimension_lines.get(2);
		dimensionA.setOffset(dimensionA.getOffset() * 2);
		dimensionB.setOffset(dimensionB.getOffset() * 2);
		// add dimensions for best combination A
		DimensionLine dimensionBestA, dimensionBestB, dimensionSpaceA, dimensionSpaceB;
		if(C1 >= C2) {
			dimensionBestA = new DimensionLine(0, 0, bestA.sum + shortSide, 0, dimensionA.getOffset()/2);
			dimensionBestB = new DimensionLine(0, 0, 0, bestB.sum, dimensionB.getOffset()/2);
			dimensionSpaceA = new DimensionLine(bestA.sum + shortSide, 0, A, 0, dimensionA.getOffset()/2);
			dimensionSpaceB = new DimensionLine(0, bestB.sum, 0, B, dimensionB.getOffset()/2);
		} else {
			dimensionBestA = new DimensionLine(A - (bestA.sum + shortSide), 0, A, 0, dimensionA.getOffset()/2);
			dimensionBestB = new DimensionLine(A, 0, A, bestB.sum, dimensionB.getOffset()/2);
			dimensionSpaceA = new DimensionLine(0, 0, A - (bestA.sum + shortSide), 0, dimensionA.getOffset()/2);
			dimensionSpaceB = new DimensionLine(A, bestB.sum, A, B, dimensionB.getOffset()/2);
		}
		getHome().addDimensionLine(dimensionBestA);
		getHome().addDimensionLine(dimensionBestB);
		getHome().addDimensionLine(dimensionSpaceA);
		getHome().addDimensionLine(dimensionSpaceB);
	}
	
	protected void drawRacks(float A, float B, float C1, float C2, String unit) {
		// normalize all values to centimeters
		A = normalize(A, unit);
		B = normalize(B, unit);
		C1 = normalize(C1, unit);
		C2 = normalize(C2, unit);
		
		float shortSide = (C1 >= C2) ? calculateShortSide(C1) : calculateShortSide(C2);

		Float[] numbers = {90f, 120f, 150f, 180f};
		ArrayList<Float> longSides = new ArrayList<Float>(Arrays.asList(numbers));
		// not using 30x180 racks
		if (shortSide == 30f) {
			longSides.remove(longSides.size()-1);
		}
		
		Combination bestA = bestCombination(combinations(longSides, A - shortSide));
		Combination bestB = bestCombination(combinations(longSides, B));
		
		if (bestB != null)
		{
			System.out.println("Best combination for B");
			System.out.println(bestB.toString());
			drawRacksB(C1, C2, bestB, A);
		}
		if (bestA != null)
		{
			System.out.println("Best combination for A");
			System.out.println(bestA.toString());
			drawRacksA(C1, C2, bestA, A);
		}
		
		addDimensionLinesRacks(C1, C2, bestA, bestB, A, B, shortSide);
		
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
		textFieldC1.setText(String.valueOf(0));
		JPanel panelC1 = new JPanel();
		panelC1.setLayout(new BorderLayout());
		panelC1.setBorder(BorderFactory.createEmptyBorder(0,0,5,0));
		panelC1.add(labelC1, BorderLayout.LINE_START);
		panelC1.add(textFieldC1, BorderLayout.LINE_END);
		fieldsPnl.add(panelC1);
		
		JLabel labelC2 = new JLabel("C2");
		labelC2.setBorder(BorderFactory.createEmptyBorder(0,0,0,5));
		JTextField textFieldC2 = new JTextField(TEXT_FIELD_SIZE);
		textFieldC2.setText(String.valueOf(0));
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
		JTextArea footnoteArea = new JTextArea(2, TEXT_FIELD_SIZE);
		footnoteArea.setFont(UIManager.getFont("TextField.font"));
		footnoteArea.setText("Not drawn to Scale" + System.lineSeparator() + "Measurements made by customer");
		JScrollPane footnoteSp = new JScrollPane(footnoteArea);
		JPanel footnotePnl = new JPanel();
		footnotePnl.setLayout(new BorderLayout());
		footnotePnl.setBorder(BorderFactory.createEmptyBorder(0,0,5,0));
		footnotePnl.add(labelFootnote, BorderLayout.LINE_START);
		footnotePnl.add(footnoteSp, BorderLayout.LINE_END);
		fieldsPnl.add(footnotePnl);
		
		JLabel labelFloatingtext = new JLabel("Floating text");
		labelFloatingtext.setBorder(BorderFactory.createEmptyBorder(0,0,0,5));
		JTextField floatingTf = new JTextField(TEXT_FIELD_SIZE);
		floatingTf.setText("©StoreroomRackSG.com");
		JPanel floatingTextPnl = new JPanel();
		floatingTextPnl.setLayout(new BorderLayout());
		floatingTextPnl.setBorder(BorderFactory.createEmptyBorder(0,0,5,0));
		floatingTextPnl.add(labelFloatingtext, BorderLayout.LINE_START);
		floatingTextPnl.add(floatingTf, BorderLayout.LINE_END);
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
					float C1 = (textFieldC1.getText().isEmpty() ? 0f : Float.parseFloat(textFieldC1.getText()));
					float C2 = (textFieldC2.getText().isEmpty() ? 0f : Float.parseFloat(textFieldC2.getText()));
					String title = textFieldTitle.getText();
					String footnote = footnoteArea.getText();
					String floatingtext = floatingTf.getText();
					String option = group.getSelection().getActionCommand();
					float footnoteTextLines = footnoteArea.getLineCount();
					drawRoom(A, B, C1, C2, option, title, footnote, floatingtext, footnoteTextLines);
					if (C1 < 30 && C2 < 30) {
						JOptionPane.showMessageDialog(null, "Cannot draw boxes with C1 = "+ C1 + " and C2 = " + C2 + System.lineSeparator() + "Please enter values greater than 30 for C1 or C2", 
								"C1 and C2 less than 30", JOptionPane.INFORMATION_MESSAGE);
					} else {
						drawRacks(A, B, C1, C2, option);
					}
					dialog.dispose();
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "Fields A and B require numbers."+ System.lineSeparator() + ex.getMessage(), 
							"Numbers error", JOptionPane.ERROR_MESSAGE);
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
		dialog.getRootPane().setDefaultButton(createRoomBtn);
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
