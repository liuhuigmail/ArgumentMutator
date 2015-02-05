package argumentMutator.mutation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;

import argumentMutator.model.CandidateModel;
import argumentMutator.model.VariableModel;
import argumentMutator.utils.RandomUtil;

public class AssignmentMutation extends ASTNodeMutation {
	private Assignment assignment = null;

	public AssignmentMutation(Assignment assignment,
			List<CandidateModel> typeMembers) {
		super(assignment, typeMembers);
		this.assignment = assignment;
	}

	public String[] mutate() {
		Expression leftExp = assignment.getLeftHandSide();
		VariableModel leftVariable = new VariableModel(leftExp);
		Expression rightExp = assignment.getRightHandSide();
		VariableModel rightVariable = new VariableModel(rightExp);

		boolean changed = false;
		if (checkNeedMutation(leftVariable, rightVariable)) {
			if (rightVariable.qualifierTypeBinding == null) {
				changed = mutateLocalVariable(leftVariable, rightVariable);
			} else {
				changed = mutateTypeMember(rightVariable);
			}
		}
		Expression rightExp1 = assignment.getRightHandSide();
		VariableModel rightVariable1 = new VariableModel(rightExp1);
		String mutationName = rightVariable1.name;
		String[] rebackName = new String[2];
		rebackName[0]  = changed ? assignment.toString() : null;
		rebackName[1] = changed ? mutationName : null;
		return rebackName;
	}
	
	
	
	private boolean mutateLocalVariable(VariableModel leftVariable,
			VariableModel rightVariable) {
		List<CandidateModel> candidates = new ArrayList<CandidateModel>();
		candidates.addAll(localVariables);
		if(typeMembers != null){
			candidates.addAll(typeMembers);
		}
		
		candidates = getRequiredCandidates(candidates, rightVariable.typeBinding,
				rightVariable, false);
		for (CandidateModel candidate : candidates) {
			if (candidate.equals(leftVariable)) {
				candidates.remove(candidate);
				break;
			}
		}
		int size = candidates.size();
		if (size <= 0) {
			return false;
		}
		int index = RandomUtil.getRandomInt(size);
		CandidateModel candidate = candidates.get(index);

		AST ast = assignment.getAST();
		if (!candidate.isMethod) {
			SimpleName replacement = ast.newSimpleName(candidate.name);
			assignment.setRightHandSide(replacement);
		} else {
			MethodInvocation replacement = ast.newMethodInvocation();
			SimpleName simpleName = ast.newSimpleName(candidate.name);
			replacement.setName(simpleName);
			assignment.setRightHandSide(replacement);
		}

		return true;
	}

	private boolean mutateTypeMember(VariableModel variable) {
		List<CandidateModel> candidates = getTypeMembers(variable);
		candidates = getRequiredCandidates(candidates, variable.typeBinding,
				variable, true);
		int size = candidates.size();
		if (size <= 0) {
			return false;
		}
		int index = RandomUtil.getRandomInt(size);
		CandidateModel candidate = candidates.get(index);

		AST ast = assignment.getAST();
		if (!candidate.isMethod) {
			Name name = ast.newName(candidate.qualifier);
			SimpleName simpleName = ast.newSimpleName(candidate.name);
			QualifiedName replacement = ast.newQualifiedName(name, simpleName);
			assignment.setRightHandSide(replacement);
		} else {
			MethodInvocation replacement = ast.newMethodInvocation();
			Name name = ast.newName(candidate.qualifier);
			SimpleName simpleName = ast.newSimpleName(candidate.name);
			replacement.setExpression(name);
			replacement.setName(simpleName);
			assignment.setRightHandSide(replacement);
		}

		return true;
	}
}
