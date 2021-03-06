package org.jgrapht.experimental.clustering;

import org.jgrapht.experimental.clustering.krv.KRVPotential;
import org.jgrapht.experimental.clustering.krv.MatchingStep;
import org.jgrapht.experimental.clustering.krv.ModifiedEfficientKRVProcedure;
import org.jgrapht.experimental.clustering.krv.PracticalVertexDivider;

/**
 * Parameters for the decomposition algorithm.
 * 
 * @author moritzfuchs
 * @date 12.09.2013
 *
 */
public class DecompositionConstants {

	//Prevent initialization
	private DecompositionConstants() {}

	/**
	 * Turn debugging mode on/off. 'On' will be very slow since a lot of stuff is double-checked / recomputed.
	 */
	public static final Boolean DEBUG = false;
	
	/**
	 * Turns 'light debugging mode' on/off. In contrast to DEBUG is does not affect the runtime too much.
	 */
	public static final Boolean LIGHT_DEBUG = false;
	
	/**
	 * Mainly for debugging: error that we are allowed to make (due to Double precision)
	 */
	public static final Double EPSILON = 0.000001;
	
	/**
	 * In order to be a balanced clustering, the subset of the vertices defining the clustering must be smaller 
	 * than the size of the underlying graph times this constant. 
	 */
	public static final Double BALANCED_CLUSTERING_BOUND = 3.0 / 4.0;
	
	/**
	 * Used by the {@link FlowRescaler}. All flow paths that are bigger than the bound will be rescaled to have a weight of exactly 1.0
	 */
	public static final Double RESCALE_BOUND = 0.5;
	
	/**
	 * Used by {@link KRVPotential}. Number of iterations that are used to approximate the potential. (bigger => more space + time needed!)
	 */
	public static final Integer POTENTIAL_APPROXIMATION_ITERATIONS = 100;
	
	/**
	 * User by {@link MatchingStep}. Fraction of the flow vector that is moved during a matching.
	 */
	public static final Double FLOW_MOVEMENT_FRACTION = 0.5;
	
	/**
	 * Used by {@link ModifiedEfficientKRVProcedure}. The procedure restarts when the set of edges F = A+B gets smaller than this constant times the original F. 
	 */
	public static final Double KRV_RESTART_BOUND = 7.0/8.0;
	
	/**
	 * Used by {@link PracticalVertexDivider} and {@link TheoreticalVertexDivider}. Maximal fraction of the edge set that can become source edges.
	 */
	public static final Double MAX_SOURCE_EDGES = 1.0/8.0;
	
	/**
	 * Multiple of the available CPU Cores that are used. #Cores * this constant = number of Threads in ThreadPool.
	 */
	public static final Integer MULTIPLE_OF_CORES = 2;
	
	/**
	 * Number of applications of a matching step.
	 */
	public static final Integer MATCHING_APPLICATIONS = 15;
	
	/**
	 * Statistics are gathered if this constant is set to true
	 */
	public static final Boolean STATS = false;
	
	/**
	 * Number of iterations the 'No Progress' heuristic looks at
	 */
	public static final Integer NO_PROGRESS_BREAK_CONDITION_ITERATIONS = 50;
	
	/**
	 * Indicates how small clusters (<= size 8) are handled. If true a brute force bisection is computed, if false the usual procedure is applied 
	 * (there might be some problems if the break condition is not chosen right - you will need the 'NoProgress' break condition in order to make this work)
	 */
	public static final Boolean BRUTE_FORCE_BISECTION = true;
}
