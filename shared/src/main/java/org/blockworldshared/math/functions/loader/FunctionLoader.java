/**
 * FunctionLoader Author: Matt Teeter May 20, 2012
 */
package org.blockworldshared.math.functions.loader;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

/**
 * @author Matt Teeter
 */
public class FunctionLoader {
    private final Logger LOG = LoggerFactory.getLogger(FunctionLoader.class);

    public void loadFunctions() throws JAXBException {
        final JAXBContext ctx = JAXBContext.newInstance(new Class[] { Functions.class });
        final Unmarshaller um = ctx.createUnmarshaller();
        final Functions functions = (Functions) um.unmarshal(new File("src/function.xml"));
        LOG.debug(functions.toString());
    }

    /**
     * @param args
     */
    public static void main(final String[] args) {
        try {
            new FunctionLoader().loadFunctions();
        } catch (final JAXBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
