package org.deidentifier.arx.gui.view.impl.wizard.sharingwizard.evaluation;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.swtchart.*;
import org.swtchart.ISeries.*;
import org.deidentifier.arx.gui.Controller;
import org.deidentifier.arx.gui.resources.Resources;
import org.deidentifier.arx.gui.view.impl.wizard.sharingwizard.checklist.Checklist;
import org.deidentifier.arx.gui.view.impl.wizard.sharingwizard.checklist.Question;
import org.deidentifier.arx.gui.view.impl.wizard.sharingwizard.checklist.Section;

/**
 * the stack visualization
 *
 */
public class StackVisualization extends Visualization {
	/**
	 * the displayed chart
	 */
	private Chart chart;
	
	/**
	 * the bar series for the positive values
	 */
	private IBarSeries positive;
	
	/**
	 * the bar series for the neutral values
	 */
	private IBarSeries neutral;
	
	/**
	 * the bar series for the negative values
	 */
	private IBarSeries negative;

	
	/**
	 * create a new stack visualization
	 * @param parent the parent
	 * @param controller the controller
	 * @param checklist the checklist
	 */
	public StackVisualization(Composite parent, Controller controller, Checklist checklist) {
		super(parent, controller, checklist);
	}

	/**
	 * creates the view containing the stack bar graph visualization
	 */
	protected void createVisualization() {
		Section sections[] = this.checklist.getSections();
		String sectionNames[] = new String[sections.length];
		int idx = 0;
		for(Section s : sections) {
			sectionNames[idx] = s.getTitle();
			idx++;
		}

		FillLayout fillLayout = new FillLayout();
		fillLayout.type = SWT.VERTICAL;
		this.setLayout(fillLayout);
		chart = new Chart(this, SWT.NONE);
		chart.getTitle().setText(Resources.getMessage("RiskWizard.18"));
		
		double[] positiveSeries = { 0.1, 0, 0};
		double[] neutralSeries = { 0.1, 0, 0};
		double[] negativeSeries = { 0.1, 0, 0};
		
		ISeriesSet seriesSet = chart.getSeriesSet();
		
		positive = (IBarSeries)seriesSet.createSeries(SeriesType.BAR, Resources.getMessage("RiskWizard.15"));
		positive.setBarColor(this.getDisplay().getSystemColor(SWT.COLOR_GREEN));
		positive.enableStack(true);
		positive.setYSeries(positiveSeries);
		
		neutral = (IBarSeries)seriesSet.createSeries(SeriesType.BAR, Resources.getMessage("RiskWizard.16"));
		neutral.setBarColor(this.getDisplay().getSystemColor(SWT.COLOR_GRAY));
		neutral.enableStack(true);
		neutral.setYSeries(neutralSeries);
		
		negative = (IBarSeries)seriesSet.createSeries(SeriesType.BAR, Resources.getMessage("RiskWizard.17"));
		negative.setBarColor(this.getDisplay().getSystemColor(SWT.COLOR_RED));
		negative.enableStack(true);
		negative.setYSeries(negativeSeries);
		
		IAxisSet axisSet = chart.getAxisSet();
		axisSet.adjustRange();
		
		IAxis yAxis = axisSet.getYAxis(0);
		yAxis.setRange(new Range(0.0, 1.05));
		yAxis.getTitle().setVisible(false);
		
		IAxis xAxis = axisSet.getXAxis(0);
		xAxis.setCategorySeries(sectionNames);
		xAxis.enableCategory(true);
		xAxis.getTitle().setVisible(false);
	}

	/**
	 * updates the UI when the weights/score changes
	 */
	public void updateWeights() {
		Section sections[] = this.checklist.getSections();
		double[] posY = new double[sections.length];
		double[] neuY = new double[sections.length];
		double[] negY = new double[sections.length];
		
		int idx = 0;
		for(Section sec : sections) {
			double pos = 0.0;
			double neu = 0.0;
			double neg = 0.0;
			
			for(Question q : sec.getItems()) {
				double w = q.getWeight();
				double s = q.getScore();
				if(s == 0.0) {
					neu += w; 
				} else if(s > 0.0) {
					pos += w;
				} else {
					neg += w;
				}
			}
			
			posY[idx] = pos / sec.getMaximumWeight();
			neuY[idx] = neu / sec.getMaximumWeight();
			negY[idx] = neg / sec.getMaximumWeight();
			
			//System.out.println("Updated "+sec.getIdentifier()+" : "+posY[idx]+" "+neuY[idx]+" "+negY[idx]);
			
			idx++;
			
			
		}
		
		
		positive.setYSeries(posY);
		positive.enableStack(true);
		neutral.setYSeries(neuY);
		neutral.enableStack(true);
		negative.setYSeries(negY);
		negative.enableStack(true);

		chart.update();
		chart.redraw();
	}
	
	
}
