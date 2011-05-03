/* 
 * JBoss, Home of Professional Open Source 
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @author tags. All rights reserved. 
 * See the copyright.txt in the distribution for a 
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use, 
 * modify, copy, or redistribute it subject to the terms and conditions 
 * of the GNU Lesser General Public License, v. 2.1. 
 * This program is distributed in the hope that it will be useful, but WITHOUT A 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details. 
 * You should have received a copy of the GNU Lesser General Public License, 
 * v.2.1 along with this distribution; if not, write to the Free Software 
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 */

package org.switchyard.extensions.wsdl;

import java.util.HashSet;
import java.util.Set;

import org.switchyard.metadata.BaseService;
import org.switchyard.metadata.ServiceOperation;

/**
 * An implementation of ServiceInterface for WSDL files.  The 
 * <code>fromWSDL()</code> method can be used to create a ServiceInterface
 * representation from a WSDL file.  A ServiceOperation will be 
 * created for each operation declared on the WSDL portType.
 * 
 * Operation names are mapped directly from the operation QName.
 * 
 * Message names are created using the input and output declarations of that operation.
 *  
 * @author Magesh Kumar B <mageshbk@jboss.com> (C) 2011 Red Hat Inc.
 */
public final class WSDLService extends BaseService {

    /**
     *  The type returned from ServiceInterface.getType().
     */
    public static final String TYPE = "wsdl";

    private static final String PORTTYPE = "wsdl.porttype";
    
    // WSDL location used to create this ServiceInterface
    private String _wsdlLocation;

    /**
     * Private constructor for creating a new ServiceInterface.  Clients of the API
     * should use fromWSDL() to create a ServiceInterface from a WSDL.
     * 
     * @param operations list of operations on the service interface
     * @param wsdlLocation the WSDL location used to derive the interface
     */
    private WSDLService(final Set<ServiceOperation> operations, final String wsdlLocation) {
        super(operations, TYPE);
        _wsdlLocation = wsdlLocation;
    }

    /**
     * Creates a ServiceInterface from the specified WSDL. The first found port definition will be used.
     * 
     * @param wsdlLocationURI the WSDL location with port name used to derive the interface
     * @return ServiceInterface representing the WSDL
     * @throws WSDLReaderException if the wsdl cannot be read or is improper
     */
    public static WSDLService fromWSDL(final String wsdlLocationURI) throws WSDLReaderException {
        int index = wsdlLocationURI.indexOf("#");
        if (index > 0) {
            String wsdlLocation = wsdlLocationURI.substring(0, index);
            String localPart = wsdlLocationURI.substring(index + 1);
            String portName = null;
            if (localPart.contains(PORTTYPE)) {
                portName = localPart.substring(PORTTYPE.length() + 1, localPart.length() - 1);
            } else {
                throw new WSDLReaderException("Invalid WSDL interface part " + wsdlLocationURI);
            }
            return fromWSDL(wsdlLocation, portName);
        } else {
            throw new WSDLReaderException("Invalid WSDL interface " + wsdlLocationURI);
        }
    }

    /**
     * Creates a ServiceInterface from the specified WSDL. The first matching port definition
     * with portName will be used.
     * 
     * @param wsdlLocation the WSDL location used to derive the interface 
     * @param portName the port name to match with
     * @return ServiceInterface representing the WSDL
     * @throws WSDLReaderException if the wsdl cannot be read or is improper
     */
    public static WSDLService fromWSDL(final String wsdlLocation, final String portName) throws WSDLReaderException {
        WSDLReader reader = new WSDLReader();
        HashSet<ServiceOperation> ops = reader.readWSDL(wsdlLocation, portName);
        return new WSDLService(ops, wsdlLocation);
    }

    /**
     * Returns the WSDL location used to create this ServiceInterface.
     * representation
     * @return Java class or interface
     */
    public String getWSDLLocation() {
        return _wsdlLocation;
    }
}