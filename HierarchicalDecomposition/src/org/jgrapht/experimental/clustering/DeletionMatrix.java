package org.jgrapht.experimental.clustering;

import cern.colt.matrix.tdouble.impl.SparseDoubleMatrix2D;

/**
 * Implementation of a DeletionMatrix
 * @author moritzfuchs
 *
 */
public class DeletionMatrix extends PartitionMatrix {

	public DeletionMatrix(SparseDoubleMatrix2D matrix) {
		super(matrix , 1);
	}
	
}
