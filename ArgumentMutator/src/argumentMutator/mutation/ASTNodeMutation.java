package argumentMutator.mutation;


import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ITypeBinding;

import argumentMutator.model.CandidateModel;
import argumentMutator.model.LocalUtil;
import argumentMutator.model.TypeUtil;
import argumentMutator.model.VariableModel;

public class ASTNodeMutation {

	public List<CandidateModel> typeMembers = null;
	public List<VariableModel> localVariables = null;

	public ASTNodeMutation(ASTNode node, List<CandidateModel> typeMembers) {
		this.typeMembers = typeMembers;
		localVariables = LocalUtil.getVariables(node);
	}

	protected boolean checkNeedMutation(VariableModel variable1,
			VariableModel variable2) {
		if (!(variable1.isLegal()&&variable2.isLegal())) {
			return false;
		}

		if (isVariableStatic(variable2)||isVariableStatic(variable1)) {
			return false;
		}

		return true;
	}

	private boolean isVariableStatic(VariableModel variable) {
		if (variable.qualifierTypeBinding != null) {
			List<VariableModel> fields = TypeUtil
					.getFields(variable.qualifierTypeBinding);
			for (VariableModel field : fields) {
				if (variable.name.equals(field.name)) {
					return false;
				}
			}
		} else {
			for (VariableModel localVariable : localVariables) {
				if(localVariable.name == null || variable.name == null)continue;
				if (variable.name.equals(localVariable.name)) {
					return false;
				}
			}
			if(typeMembers == null)return true;
			for (CandidateModel candidate : typeMembers) {
				if (!candidate.isMethod) {
					if(candidate.name == null || variable.name == null){
						continue;
					}
					if (variable.name.equals(candidate.name)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	protected List<CandidateModel> getRequiredCandidates(
			List<CandidateModel> candidates, ITypeBinding typeBinding,
			CandidateModel ignoreCandidate, boolean fromOut) {
		List<CandidateModel> requireTypeCandidates = new ArrayList<CandidateModel>();
		for (CandidateModel candidate : candidates) {
			if (!candidate.isLegal()) {
				continue;
			}
			
			if (fromOut && !candidate.externalAccessible) {
				continue;
			}
			
			if (candidate.equals(ignoreCandidate)) {
				continue;
			}
			if(candidate.isMethod){
				continue;
			}
			if (candidate.typeBinding.isAssignmentCompatible(typeBinding)) {
				requireTypeCandidates.add(candidate);
			}
		}
		return requireTypeCandidates;
	}

	protected List<CandidateModel> getTypeMembers(VariableModel variableModel) {
		ITypeBinding typeBinding = variableModel.qualifierTypeBinding;
		List<VariableModel> fields = TypeUtil.getFields(typeBinding);

		List<CandidateModel> members = new ArrayList<CandidateModel>();
		members.addAll(fields);

		for (CandidateModel member : members) {
			member.qualifier = variableModel.qualifier;
			member.qualifierTypeBinding = variableModel.qualifierTypeBinding;
		}

		return members;
	}

}
