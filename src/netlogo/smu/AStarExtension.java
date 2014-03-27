package netlogo.smu;

import org.nlogo.api.*;

public class AStarExtension extends DefaultClassManager  {

	@Override
	public void load(PrimitiveManager primitiveManager) throws ExtensionException {
		primitiveManager.addPrimitive("a-star-search", new AStarSearch());	
		primitiveManager.addPrimitive("a-star-search-with-turtle-avoidance", new AStarSearchWithTurtleAvoidance());
		primitiveManager.addPrimitive("a-star-search-with-multiple-turtle-avoidance", new AStarSearchWithMultipleTurtleAvoidance());

	}

}
