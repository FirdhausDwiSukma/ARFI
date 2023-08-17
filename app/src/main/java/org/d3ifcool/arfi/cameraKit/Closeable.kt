package org.d3ifcool.arfi.cameraKit

import java.io.Closeable

internal fun Closeable.addTo(closeables: MutableList<Closeable>) = apply { closeables.add(this) }