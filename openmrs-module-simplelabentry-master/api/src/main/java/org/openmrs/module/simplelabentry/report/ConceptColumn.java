package org.openmrs.module.simplelabentry.report;

import org.openmrs.Concept;
import org.openmrs.ConceptNumeric;
import org.openmrs.api.context.Context;


/**
 * Convenience class to hold column information for lab order report.
 *
 */
public class ConceptColumn { 
	
	private Concept concept;				
	private String displayName;
	private String value;
	private String dataType;


	/** 
	 * Constructor to be used when defining/viewing dataset.
	 * @param name
	 * @param dataType
	 */
	public ConceptColumn(Concept concept) { 
		this.concept = concept;
		this.displayName = concept.getBestShortName(Context.getLocale()).getName();
		this.dataType = concept.getDatatype().getName();
	}
	
	/** 
	 * Constructor to be used when displaying data.
	 * @param name
	 * @param value
	 * @param dataType
	 */
	public ConceptColumn(Concept concept, String value) { 
		this(concept);
		this.value = value;
	}

	public Concept getConcept() { 
		return this.concept;
	}
	
	public String getDisplayName() { 
		return this.displayName;
	}
	
	public String getValue() { 
		return this.value;
	}
	
	public String getDataType() { 			
		return this.dataType;
	}
	
	public Boolean isNumeric() { 
		return concept.isNumeric();
	}
	
	public Boolean isPrecise() { 
		if (isNumeric()) { 
		    //hack alert:  this fails:
			//return ((ConceptNumeric) concept).getPrecise();
		    ConceptNumeric cn = Context.getConceptService().getConceptNumeric(concept.getConceptId());
		    return cn.isPrecise();
		}
		return false;
	}
	
}