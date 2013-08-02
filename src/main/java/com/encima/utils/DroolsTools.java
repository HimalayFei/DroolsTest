package com.encima.utils;

import java.io.Reader;
import java.io.StringReader;

import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.ResourceType;
import org.drools.io.Resource;
import org.drools.io.ResourceFactory;

public class DroolsTools {

	public static void addRule(KnowledgeBuilder kbuilder, String rule) {
		Resource myResource = ResourceFactory.newReaderResource((Reader) new StringReader(rule));
        kbuilder.add(myResource, ResourceType.DRL);
	}
	
}
