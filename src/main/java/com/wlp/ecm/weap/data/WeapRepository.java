package com.wlp.ecm.weap.data;

import org.springframework.expression.EvaluationContext;
import com.wlp.ecm.weap.reporting.MessageData;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import com.filenet.api.core.ObjectStore;
import com.wlp.ecm.weap.common.LoggingUtil;
import com.wlp.ecm.weap.document.WeapDocument;
import com.wlp.ecm.weap.exception.WeapException;

public abstract class WeapRepository 
{
	public abstract String storeDocument(WeapDocument weapDocument,	WeapMetadata metadata, int version,  
										 MessageData msgData, String docSection) throws WeapException;

	protected <T> T evaluateProperty(WeapMetadata metadata, String weapPropertyName, Class<T> desiredResultType) 
	{
		LoggingUtil.LogTraceStartMsg();

		ExpressionParser parser = new SpelExpressionParser();
		Expression expression = parser.parseExpression(weapPropertyName);
		EvaluationContext ectx = new StandardEvaluationContext(metadata);
		T value = expression.getValue(ectx, desiredResultType);
		
		LoggingUtil.LogTraceEndMsg();
		return value;
	}
}
