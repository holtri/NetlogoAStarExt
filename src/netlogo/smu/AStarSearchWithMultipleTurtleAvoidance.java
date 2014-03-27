package netlogo.smu;

import java.util.HashSet;
import java.util.Set;

import org.nlogo.agent.Agent;
import org.nlogo.api.AgentException;
import org.nlogo.api.AgentSet;
import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.LogoException;
import org.nlogo.api.Patch;
import org.nlogo.api.Syntax;

public class AStarSearchWithMultipleTurtleAvoidance extends AStarSearch{

	private Set<Patch> blockedPatches;
	
	@Override
	public Syntax getSyntax() {
		return Syntax.reporterSyntax(
				new int[] { Syntax.TurtleType(), Syntax.PatchsetType(), Syntax.AgentsetType()}, Syntax.ListType());
	}
	
	@Override
	public Object report(Argument[] arguments, Context context)
			throws ExtensionException, LogoException {
		AgentSet blockingAgents = arguments[2].getAgentSet();
		blockedPatches = new HashSet<Patch>();
		java.util.Iterator<org.nlogo.api.Agent> iter = blockingAgents.agents().iterator();
		while(iter.hasNext()){
			org.nlogo.api.Agent agent = iter.next();
			try {
				blockedPatches.add((((Agent) agent).getPatchAtOffsets(0, 0)));
			} catch (AgentException e) {
				e.printStackTrace();
			}
		}
		return super.report(arguments, context);
	}

	@Override
	protected void addPatchToOpenList(CandidatePatch currentPatch,
			Patch successor, double realValueSuccessor) throws LogoException {
		if(!blockedPatches.contains(successor)){
			super.addPatchToOpenList(currentPatch, successor, realValueSuccessor);
		}
		
	}
}
