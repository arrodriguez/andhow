package yarnandtail.andhow.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import yarnandtail.andhow.ConfigPoint;
import yarnandtail.andhow.Loader;
import yarnandtail.andhow.LoaderValues;
import yarnandtail.andhow.ValueMap;
import yarnandtail.andhow.ValueMapWithContext;

/**
 *
 * @author eeverman
 */
public class ValueMapWithContextImmutable extends ValueMapWithContextBase {
	
	/** Lists of values loaded by each loader */
	private final ArrayList<LoaderValues> structuredValues = new ArrayList();
	
	/** Just the final effective values */
	private final ValueMap effectiveValues;
	
	private final boolean problem;
		
	public ValueMapWithContextImmutable(List<LoaderValues> inLoadedValuesList) {
		structuredValues.addAll(inLoadedValuesList);
		structuredValues.trimToSize();
		effectiveValues = super.buildValueMapImmutable(structuredValues);
		
		//Check for problems
		boolean willHaveProblem = false;
		for (LoaderValues lvs : structuredValues) {
			if (lvs.hasProblems()) {
				willHaveProblem = true;
				break;
			}
		}
		
		problem = willHaveProblem;
	}

	//
	//These next three methods use the ValueMap b/c it is backed by a HashMap
	@Override
	public <T> T getExplicitValue(ConfigPoint<T> point) {
		return effectiveValues.getExplicitValue(point);
	}
	
	@Override
	public <T> T getEffectiveValue(ConfigPoint<T> point) {
		return effectiveValues.getEffectiveValue(point);
	}

	@Override
	public boolean isExplicitlySet(ConfigPoint<?> point) {
		return effectiveValues.isExplicitlySet(point);
	}
	
	@Override
	public List<LoaderValues> getAllLoaderValues() {
		return Collections.unmodifiableList(structuredValues);
	}
	
	//
	//These methods use list searching and are comparatively slow
	
	@Override
	public LoaderValues getAllValuesLoadedByLoader(Loader loader) {
		return getAllValuesLoadedByLoader(structuredValues, loader);
	}

	@Override
	public LoaderValues getEffectiveValuesLoadedByLoader(Loader loader) {
		return getEffectiveValuesLoadedByLoader(structuredValues, loader);
	}
	
	//
	//Build methods

	@Override
	public ValueMap getValueMapImmutable() {
		return effectiveValues;
	}
	
	@Override
	public ValueMapWithContext getValueMapWithContextImmutable() {
		return this;
	}
	
	@Override
	public boolean hasProblems() {
		return problem;
	}
}