package org.deidentifier.arx.gui.view.impl.wizard.sharingwizard;

import java.text.DecimalFormat;
import java.text.ParseException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import org.deidentifier.arx.gui.view.impl.wizard.sharingwizard.checklist.Item;

/**
 * a dropdown field for changing a question's weight
 *
 */
public class WeightField {
	/**
	 * the targeted item
	 */
	private Item item;
	
	private Combo dropdown;
	private boolean disableUpdateQuestion;
	
	private boolean enabled;
	
	static final DecimalFormat df = new DecimalFormat("#.00"); 
	
	/**
	 * create a new weight field for an item (question or section)
	 * @param composite the composite
	 * @param item the item this field targets
	 * @param enabled whether this control is currently enabled
	 */
	public WeightField(Composite composite, Item item, boolean enabled) {		
		dropdown = new Combo(composite, SWT.DROP_DOWN | SWT.READ_ONLY | SWT.BORDER);
		dropdown.add("0.25");
		dropdown.add("0.50");
		dropdown.add("1.00");
		dropdown.add("1.50");
		dropdown.add("2.00");
		dropdown.add("3.00");
		dropdown.add("5.00");
		
		WeightField reference = this;
		
		ModifyListener modifyListener = new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				reference.updateItem();
			}
		};
		
		dropdown.addModifyListener(modifyListener);
		
		this.item = item;
		this.setEnabled(enabled);
		
		GridData gridData = new GridData(GridData.VERTICAL_ALIGN_CENTER);
		gridData.widthHint = 72;
		dropdown.setLayoutData(gridData);
		
		
		updateText();
	}
	
	
	/**
	 * updates the text of the dropdown
	 */
	public void updateText() {
		disableUpdateQuestion = true;
		double newWeight = item.getWeight();
		String weightString = df.format(newWeight);
		//System.out.println("Weight: "+weightString+" item="+item.getIdentifier());
		
		boolean containsText = false;
		for(String t : dropdown.getItems()) {
			if(t.equals(weightString)) {
				containsText = true;
			}
		}
		if(containsText == false) {
			dropdown.add(weightString);
		}
		
		dropdown.setText(df.format(newWeight));
		disableUpdateQuestion = false;
	}
	
	/**
	 * updates the target item
	 */
	protected void updateItem() {
		if(disableUpdateQuestion) {
			return;
		}
		try {
			double value = df.parse(dropdown.getText()).doubleValue();
			item.setWeight(value);
		} catch (ParseException e) {
			//e.printStackTrace();
		}
	}

	/**
	 * returns, whether the field is editable
	 * @return if field can be edited
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * change whether field can be edited
	 * @param enabled whether field can be edited
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		this.dropdown.setEnabled(enabled);
	}

}
