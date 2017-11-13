package org.yarnandtail.andhow.load;

import java.util.*;
import org.yarnandtail.andhow.PropertyValue;
import org.yarnandtail.andhow.api.*;

/**
 * A utility loader that is used internally to put fixed values into the effective
 * list of values.
 * 
 * This loader does not trim incoming values for String type properties - they are
 * assumed to already be in final form.
 * This loader considers it a problem to be passed unrecognized properties
 * and will throw a RuntimeException if that happens.
 * 
 * @author eeverman
 */
public class FixedValueLoader extends BaseLoader implements ReadLoader {

	List<PropertyValue> values;
			
	public FixedValueLoader(List<PropertyValue> values) {
		this.values = values;
	}
	
	public FixedValueLoader(PropertyValue... values) {
		this.values = Arrays.asList(values);
	}
	
	@Override
	public LoaderValues load(StaticPropertyConfiguration appConfigDef, ValidatedValuesWithContext existingValues) {
		
		if (values != null && values.size() > 0) {
			List<ValidatedValue> vvs = new ArrayList(values.size());
			
			for (int i = 0; i < values.size(); i++) {
				ValidatedValue vv = new ValidatedValue(values.get(i).getProperty(), values.get(i).getValue());
				vvs.add(vv);
			}
			
			return new LoaderValues(this, vvs, ProblemList.EMPTY_PROBLEM_LIST);
			
		} else {
			return new LoaderValues(this, Collections.emptyList(), ProblemList.EMPTY_PROBLEM_LIST);
		}
		
	}
	
	@Override
	public boolean isTrimmingRequiredForStringValues() {
		return false;
	}
	
	@Override
	public boolean isUnrecognizedPropertyNamesConsideredAProblem() {
		return true;
	}
	
	@Override
	public String getSpecificLoadDescription() {
		return "a list of fixed values passed in by the construction code (not dynamically loaded)";
	}
	
	@Override
	public String getLoaderType() {
		return "FixedValue";
	}
	
	@Override
	public String getLoaderDialect() {
		return "FromJavaSourceCode";
	}
	
}
