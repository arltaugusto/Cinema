package com.project.utils;

import java.io.Serializable;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;


public class IdGenerator implements IdentifierGenerator {

	public static final String generatorName = "IdGenerator";

	@Override
	public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object object)
			throws HibernateException {
		return UUID.randomUUID().toString().replace("-", StringUtils.EMPTY);
	}
}
