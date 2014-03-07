package netlogo.smu;

import java.util.HashSet;

import org.nlogo.api.Patch;

public class PatchUtil {

	public static double caldulateEuclideanDistance(Patch patch1, Patch patch2){
		return Math.sqrt(Math.pow(
				(patch1.pxcor() - patch2.pxcor()), 2)
				+ Math.pow((patch1.pycor() - patch2.pycor()), 2));
	}
	
	public static double calculateClosestEuclideanDistance(HashSet<Patch> targets, Patch patch){
		double result = Double.MAX_VALUE;
		for(Patch p : targets){
			double temp = caldulateEuclideanDistance(p, patch);
			if(temp < result){
				result = temp;
			}
		}
		return result;
	}
}
