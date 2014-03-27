package netlogo.smu;

import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.LogoException;
import org.nlogo.api.Patch;
import org.nlogo.api.Syntax;

public class AStarSearchWithTurtleAvoidance extends AStarSearch {


	private Patch blockedPatch;

	@Override
	public Syntax getSyntax() {
		return Syntax.reporterSyntax(
				new int[] { Syntax.TurtleType(), Syntax.PatchsetType(), Syntax.PatchType()}, Syntax.ListType());
	}
	
	@Override
	public Object report(Argument[] arguments, Context context)
			throws ExtensionException, LogoException {
		blockedPatch = arguments[2].getPatch();
		
		return super.report(arguments, context);
	}

	@Override
	protected void addPatchToOpenList(CandidatePatch currentPatch,
			Patch successor, double realValueSuccessor) throws LogoException {
		if(successor.id()!= blockedPatch.id()){
			super.addPatchToOpenList(currentPatch, successor, realValueSuccessor);
		}
		
	}

	
}
