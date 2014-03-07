package netlogo.smu;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import org.nlogo.api.AgentException;
import org.nlogo.api.LogoList;
import org.nlogo.api.LogoListBuilder;
import org.nlogo.api.Patch;

public class CandidatePatch {
	private Patch patch;
	
	private CandidatePatch predecessor;

	public Patch getPatch() {
		return patch;
	}

	private double heuristicValue;
	private double realValue;

	private List<Patch> neighbors;

	public CandidatePatch(Patch patch, HashSet<Patch> targets, double realValue, CandidatePatch predecessor) {
		this.patch = patch;
		this.realValue = realValue;
		this.heuristicValue = PatchUtil.calculateClosestEuclideanDistance(targets, patch);
		this.predecessor = predecessor;
	}
	
	public LogoList getPath(){
		LogoListBuilder path = new LogoListBuilder();
		return getPathToPredecessor(path).toLogoList();
	}
	
	private LogoListBuilder getPathToPredecessor(LogoListBuilder path) {
		if(this.predecessor != null){
			this.predecessor.getPathToPredecessor(path);
		}
		path.add(this.patch);
		return path;
	}

	public double getEstimatedTotalDistance() {
		return heuristicValue + realValue;
	}

	public double getRealValue() {
		return realValue;
	}

	public Long getPatchID() {
		return patch.id();
	}

	public String toString() {
		return "" + this.patch.id() + "(" + this.patch.pxcor() + ","
				+ this.patch.pycor() + ")";
	}

	public List<Patch> getNeighbors() {
		List<Patch> neighbors = this.neighbors;
		if (neighbors == null) {
			neighbors = new ArrayList<Patch>();
			buildNeighborList(neighbors);
			this.neighbors = neighbors;
		}
		return neighbors;
	}

	private void buildNeighborList(List<Patch> neighbors) {
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= +1; j++) {
				addPatchToNeighbors(neighbors, i, j);
			}
		}
	}

	private void addPatchToNeighbors(List<Patch> neighbors, int i, int j) {
		if (!(i == 0 && j == 0)) {
			try {
				neighbors.add(this.patch.getPatchAtOffsets(i, j));
			} catch (AgentException e) {
				e.printStackTrace();
			}
		}
	}

	public static class TotalDistanceComparator implements
			Comparator<CandidatePatch> {

		@Override
		public int compare(CandidatePatch patch1, CandidatePatch patch2) {
			if (patch1.getEstimatedTotalDistance()
					- patch2.getEstimatedTotalDistance() < 0) {
				return -1;
			} else if (patch1.getEstimatedTotalDistance()
					- patch2.getEstimatedTotalDistance() > 0) {
				return 1;
			} else
				return 0;
		}

	}

}
