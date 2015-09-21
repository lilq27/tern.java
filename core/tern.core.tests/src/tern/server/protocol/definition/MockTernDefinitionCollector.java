/**
 *  Copyright (c) 2013-2015 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package tern.server.protocol.definition;

import tern.server.protocol.definition.ITernDefinitionCollector;

public class MockTernDefinitionCollector implements ITernDefinitionCollector {

	private String file;
	private Long start;
	private Long end;

	@Override
	public void setDefinition(String file, Long start, Long end) {
		this.file = file;
		this.start = start;
		this.end = end;
	}

	public String getFile() {
		return file;
	}

	public Long getStart() {
		return start;
	}

	public Long getEnd() {
		return end;
	}
}
