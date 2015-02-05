package argumentMutator.mutation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;

import argumentMutator.global.Config;
import argumentMutator.model.CandidateModel;
import argumentMutator.model.MethodUtil;
import argumentMutator.model.VariableModel;
import argumentMutator.utils.RandomUtil;


public class MethodMutation extends ASTNodeMutation {

	private MethodInvocation methodInvocation;
	
	private boolean isIgnore = false;

	public MethodMutation(MethodInvocation methodInvocation,
			List<CandidateModel> typeMembers) {
		super(methodInvocation, typeMembers);
		this.methodInvocation = methodInvocation;
	}

	public String mutate() {
		String rebackName = null;

		List<VariableModel> arguments = MethodUtil
				.getArguments(methodInvocation);
		List<VariableModel> parameters = MethodUtil
				.getParameters(methodInvocation);
		if (arguments == null || parameters == null
				|| arguments.size() == 0 || arguments.size() != parameters.size()) {
			return rebackName;
		}
		int i = 0;
		int j = 0;
		for (VariableModel argument : arguments) {
			VariableModel parameter = parameters.get(i);
			if(Config.contains(parameter.name)){
				j++;
			}
			i++;
		}
		
		if (!RandomUtil.getRandomBooleanByPercent((parameters.size()-j)*Config.MUTATION_PROBABILITY)) {
			return rebackName;
		}
		
		int k = 0;
		while(k < 20){
			int index;
			k++;
			if(parameters.size() == 1){
				index = 0;
			} else {
				Random rand = new Random();
				index = rand.nextInt(parameters.size()-1);
			}			
			VariableModel parameter = parameters.get(index);
			if(Config.contains(parameter.name)){
				continue;
			}
			boolean changed = false;
			VariableModel argument = arguments.get(index);
			if (checkNeedMutation(parameter, argument)) {
				if (argument.qualifierTypeBinding == null) {
					changed = mutateLocalVariable(argument, index);
				} else {
					changed = mutateTypeMember(argument, index);
				}
			}
			
			if (changed) {				
				rebackName = methodInvocation.toString();
				break;
			}
			
		}
		
//		boolean changed = false;
//		int index = 0;
//		
//		for (VariableModel argument : arguments) {
//			VariableModel parameter = parameters.get(index);
//			if(Config.contains(parameter.name)){
//				index++;
//				continue;
//			}
//			if (checkNeedMutation(parameter, argument)) {
//				if (argument.qualifierTypeBinding == null) {
//					changed = mutateLocalVariable(argument, index);
//				} else {
//					changed = mutateTypeMember(argument, index);
//				}
//			}
//			index++;
//			if (changed) {
//				rebackName[0] = methodInvocation.toString();
//				rebackName[1] = parameter.name;
//				rebackName[2] = argument.name;
//				rebackName[3] = MethodUtil.getArguments(methodInvocation).get(index-1).name;
//				break;
//			}
//		}

		return rebackName;
	}

	private boolean mutateLocalVariable(VariableModel variable, int index) {
		List<CandidateModel> candidates = new ArrayList<CandidateModel>();
		candidates.addAll(localVariables);
		if(typeMembers != null){
			candidates.addAll(typeMembers);
		}
		candidates = getRequiredCandidates(candidates, variable.typeBinding,
				variable, false);

		int size = candidates.size();
		if (size <= 0) {
			return false;
		}
		int randomIndex = RandomUtil.getRandomInt(size);
		CandidateModel candidate = candidates.get(randomIndex);	
		
		AST ast = methodInvocation.getAST();
		if (!candidate.isMethod) {
			SimpleName replacement = ast.newSimpleName(candidate.name);
			MethodUtil.replaceArgument(methodInvocation, index, replacement);
		} else {
			MethodInvocation replacement = ast.newMethodInvocation();
			SimpleName simpleName = ast.newSimpleName(candidate.name);
			replacement.setName(simpleName);
			MethodUtil.replaceArgument(methodInvocation, index, replacement);
		}
		return true;
	}

	private boolean mutateTypeMember(VariableModel variable, int index) {
		List<CandidateModel> candidates = getTypeMembers(variable);
		candidates = getRequiredCandidates(candidates, variable.typeBinding,
				variable, true);

		int size = candidates.size();
		if (size <= 0) {
			return false;
		}
		int randomIndex = RandomUtil.getRandomInt(size);
		CandidateModel candidate = candidates.get(randomIndex);	
		
		AST ast = methodInvocation.getAST();
		if (!candidate.isMethod) {
			Name name = ast.newName(candidate.qualifier);
			SimpleName simpleName = ast.newSimpleName(candidate.name);
			QualifiedName replacement = ast.newQualifiedName(name, simpleName);
			MethodUtil.replaceArgument(methodInvocation, index, replacement);
		} else {
			MethodInvocation replacement = ast.newMethodInvocation();
			Name name = ast.newName(candidate.qualifier);
			SimpleName simpleName = ast.newSimpleName(candidate.name);
			replacement.setExpression(name);
			replacement.setName(simpleName);
			MethodUtil.replaceArgument(methodInvocation, index, replacement);
		}

		return true;
	}
}
