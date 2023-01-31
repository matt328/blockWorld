/**
 * FunctionData Author: Matt Teeter May 22, 2012
 */
package org.blockworldshared.math.functions.loader;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.beanutils.PropertyUtils;
import org.blockworldshared.math.functions.loader.Functions.Function;
import org.blockworldshared.math.functions.loader.Functions.Function.FloatComponent;
import org.blockworldshared.math.functions.loader.Functions.Function.FunctionComponent;
import org.blockworldshared.math.functions.loader.Functions.Function.IntComponent;
import org.blockworldshared.math.functions.loader.Functions.Function.StringComponent;

import com.google.common.collect.Maps;

/**
 * @author Matt Teeter
 */
public class FunctionData {
    private final Functions functions;
    private final Map<String, org.blockworldshared.math.functions.Function> functionMap;
    private final JAXBContext jaxbContext;
    private final String filename;

    public FunctionData(final String filename) throws Exception {
        this.filename = filename;
        jaxbContext = JAXBContext.newInstance(new Class[] { Functions.class });
        final Unmarshaller um = jaxbContext.createUnmarshaller();
        try (InputStream in = getClass().getResourceAsStream(filename);) {
            functions = (Functions) um.unmarshal(in);

            functionMap = Maps.newHashMap();
            for (final Function functionData : functions.getFunction()) {
                final String typeName = functionData.getType();
                final org.blockworldshared.math.functions.Function f = (org.blockworldshared.math.functions.Function) Class
                        .forName(typeName).newInstance();
                for (final FloatComponent comp : functionData.getFloatComponent()) {
                    PropertyUtils.setProperty(f, comp.getName(), comp.getValue());
                }
                for (final IntComponent comp : functionData.getIntComponent()) {
                    PropertyUtils.setProperty(f, comp.getName(), comp.getValue());
                }
                for (final StringComponent comp : functionData.getStringComponent()) {
                    PropertyUtils.setProperty(f, comp.getName(), comp.getValue());
                }
                functionMap.put(functionData.getId(), f);
            }
            for (final Function functionData : functions.getFunction()) {
                final org.blockworldshared.math.functions.Function f = functionMap.get(functionData.getId());
                for (final FunctionComponent comp : functionData.getFunctionComponent()) {
                    PropertyUtils.setProperty(f, comp.getName(), functionMap.get(comp.getFunctionRef()));
                }
            }
        }
    }

    public void writeData() throws Exception {
        final Marshaller m = jaxbContext.createMarshaller();
        m.marshal(functions, new File(filename));
    }

    public void applyChanges() {

    }

    public Collection<Function> getAllFunctions() {
        return new ArrayList<Function>(functions.getFunction());
    }

    public void setIntParameter(final String path, final int value) throws FunctionException {
        final String functionId = getFunctionId(path);
        final String parameterName = getParameterName(path);
        final org.blockworldshared.math.functions.Function theFunction = getFunctionById(functionId);
        setFunctionIntField(theFunction, parameterName, value);
    }

    public int getIntParameter(final String path) throws FunctionException {
        final String functionId = getFunctionId(path);
        final String parameterName = getParameterName(path);
        final org.blockworldshared.math.functions.Function theFunction = getFunctionById(functionId);
        final int paramValue = getFunctionIntField(theFunction, parameterName);
        return paramValue;
    }

    public org.blockworldshared.math.functions.Function getFunctionById(final String functionId)
            throws FunctionException {
        final org.blockworldshared.math.functions.Function f = functionMap.get(functionId);
        if (f != null) {
            return f;
        }
        throw new FunctionException(String.format("Function with id: %s not found.", functionId));
    }

    private String getFunctionId(final String path) {
        final String[] arr = path.split("\\.");
        return arr[0];
    }

    private String getParameterName(final String path) {
        final String[] arr = path.split("\\.");
        return arr[1];
    }

    private int getFunctionIntField(final org.blockworldshared.math.functions.Function theFunction,
            final String fieldName) throws FunctionException {
        int value;
        try {
            value = ((Integer) PropertyUtils.getProperty(theFunction, fieldName));
        } catch (final Exception e) {
            throw new FunctionException(String.format("Error getting property %s from Function", fieldName), e);
        }
        return value;
    }

    private void setFunctionIntField(final org.blockworldshared.math.functions.Function theFunction,
            final String fieldName, final int value) throws FunctionException {
        // Set field in Functions.
    }
}
