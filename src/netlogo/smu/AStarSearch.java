package netlogo.smu;

import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import org.nlogo.api.*;
import org.nlogo.nvm.ExtensionContext;
import org.nlogo.nvm.Workspace;

public class AStarSearch extends DefaultReporter {
	
	protected static boolean debug = true;
	protected static Workspace ws;
	
	protected Queue<CandidatePatch> openList;
	protected HashSet<Patch> targets;
	protected HashSet<Long> closedList;
	
	protected void _log(String output) throws LogoException {
		if(debug){
			ws.outputObject(output, null, true, false,
					Workspace.OutputDestination.NORMAL);
		}
	}

	public Syntax getSyntax() {
		return Syntax.reporterSyntax(
				new int[] { Syntax.TurtleType(), Syntax.PatchsetType()}, Syntax.ListType());
	}

	@Override
	public Object report(Argument[] arguments, Context context)
			throws ExtensionException, LogoException {
		
		Patch startPatch = initialize(arguments, context);

		initializeAStar(startPatch);
		
		LogoList result = runAStar();
		
		if(result == null){
			result = new LogoListBuilder().toLogoList(); //return empty list to avoid nullpointer in netlogo
		}
		return result;
	}

	protected LogoList runAStar()
			throws LogoException, ExtensionException {
		LogoList result = null;
		CandidatePatch currentPatch;
		boolean targetReached = false;
		do{
			currentPatch = openList.poll();
			//check for target reached
			if(targets.contains(currentPatch.getPatch())){
				targetReached = true;
				result = currentPatch.getPath();
			}
			//expand
			if(!closedList.contains(currentPatch.getPatchID())){
				closedList.add(currentPatch.getPatchID());
				expand(currentPatch);			
			}
		}while(!openList.isEmpty() && !targetReached);
		return result;
	}

	protected void initializeAStar(Patch startPatch) {
		openList = new PriorityQueue<CandidatePatch>(8,new CandidatePatch.TotalDistanceComparator());
		closedList = new HashSet<Long>();
		openList.offer(new CandidatePatch(startPatch, targets, 0, null));
	}

	protected Patch initialize(Argument[] arguments, Context context)
			throws ExtensionException, LogoException {
		ws = ((ExtensionContext) context).workspace();
		
		Turtle person = arguments[0].getTurtle();
		AgentSet as = arguments[1].getAgentSet();
		
		initializeTargetSet(as);
		Patch patchHere = person.getPatchHere();
		return patchHere;
	}

	protected void initializeTargetSet(AgentSet as) {
		this.targets = new HashSet<Patch>();
		for(Agent agent : as.agents()){
			targets.add(((Turtle) agent).getPatchHere());
		}
	}

	protected void expand(CandidatePatch currentPatch) throws LogoException, ExtensionException {
		List<Patch> successors = currentPatch.getNeighbors();
		for(Patch successor : successors){
			if(!closedList.contains(successor.id())){
				addSuccessorToCandidateList(currentPatch, successor);
			}
		}
	}

	protected void addSuccessorToCandidateList(CandidatePatch currentPatch, Patch successor)
			throws LogoException, ExtensionException {
		double realValueSuccessor = currentPatch.getRealValue() + PatchUtil.caldulateEuclideanDistance(currentPatch.getPatch(), successor);
		int obstacleIndex = ws.world().patchesOwnIndexOf("OBSTACLE");

		if(obstacleIndex < 0){
			throw new ExtensionException("Patches need to own boolean variable 'obstacle'");
		}

		boolean isObstacle = false;
		if(successor.getVariable(obstacleIndex) instanceof Boolean){
			isObstacle = (Boolean) successor.getVariable(obstacleIndex);
		}
		if(!isObstacle){
			addPatchToOpenList(currentPatch, successor, realValueSuccessor);
		}
	}

	protected void addPatchToOpenList(CandidatePatch currentPatch, Patch successor,
			double realValueSuccessor) throws LogoException {
		openList.add(new CandidatePatch(successor, targets, realValueSuccessor, currentPatch));
	}

}
