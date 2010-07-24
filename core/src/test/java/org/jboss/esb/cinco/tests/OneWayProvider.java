/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.esb.cinco.tests;

import javax.xml.namespace.QName;

import org.jboss.esb.cinco.BaseHandler;
import org.jboss.esb.cinco.ExchangeChannel;
import org.jboss.esb.cinco.event.ExchangeInEvent;

public class OneWayProvider extends BaseHandler {
	
	private ExchangeChannel _channel;
	private int _receiveCount;
	
	public OneWayProvider(ExchangeChannel channel) {
		_channel = channel;
		_channel.getHandlerChain().addLast("me", this);
	}
	
	public void provideService(QName serviceName) {
		_channel.registerService(serviceName);
	}
	
	@Override
	public void exchangeIn(ExchangeInEvent event) {
		_receiveCount++;
		event.getExchange().done();
		_channel.send(event.getExchange());
	}

	public int getReceiveCount() {
		return _receiveCount;
	}
	
}
