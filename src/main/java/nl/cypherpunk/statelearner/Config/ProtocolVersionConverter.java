package nl.cypherpunk.statelearner.Config;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;
import de.rub.nds.tlsattacker.core.constants.ProtocolVersion;

import java.util.Arrays;

/**
 * 协议版本转换工具
 */
public class ProtocolVersionConverter implements IStringConverter<ProtocolVersion> {

	@Override
	public ProtocolVersion convert(String value) {
		try {
			return ProtocolVersion.valueOf(value);
		} catch (IllegalArgumentException e) {
			throw new ParameterException("Value " + value
					+ " cannot be converted to a ProtocolVersion. "
					+ "Available values are: "
					+ Arrays.toString(ProtocolVersion.values()));
		}
	}

}
