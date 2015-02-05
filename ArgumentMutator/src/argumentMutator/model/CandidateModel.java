package argumentMutator.model;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.SimpleName;

import argumentMutator.global.Config;
import argumentMutator.similarity.SimilarityUtil;
import argumentMutator.similarity.SimilarityUtil1;

public class CandidateModel {

	public String qualifier = null;
	public ITypeBinding qualifierTypeBinding = null;
	public String name = null;
	public ITypeBinding typeBinding = null;
	public boolean externalAccessible = false;

	public boolean isMethod = false;

	public CandidateModel() {

	}

	public CandidateModel(boolean isMethod) {
		this.isMethod = isMethod;
	}

	public boolean isLegal() {
		if (name == null) {
			return false;
		}
		if (name.length() < Config.MIN_MEMBER_NAME_LEN ) {
			return false;
		}
		if (typeBinding == null) {
			return false;
		}
		return true;
	}

	public double calculateSimilarity(CandidateModel candidate) {
		if(candidate == null ||candidate.typeBinding == null) return 0.0;
		if (!candidate.typeBinding.isAssignmentCompatible(typeBinding)) {
			return 0.0;
		}

		return SimilarityUtil1.calculate(name, candidate.name);
	}

	public Expression genExpression(AST ast) {
		Expression expression = null;
		if (isMethod) {
			MethodInvocation argument = ast.newMethodInvocation();
			argument.setName(ast.newSimpleName(name));
			if (qualifier != null) {
				argument.setExpression(ast.newName(qualifier));
			}
			expression = argument;
		} else {
			SimpleName simpleName = ast.newSimpleName(name);
			Name name = null;
			if (qualifier != null) {
				name = ast.newName(qualifier);
			}

			if (name != null) {
				expression = ast.newQualifiedName(name, simpleName);
			} else {
				expression = simpleName;
			}
		}
		return expression;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof CandidateModel)) {
			return false;
		}
		CandidateModel candidate = (CandidateModel) obj;

		boolean isEqual = (typeBinding
				.isAssignmentCompatible(candidate.typeBinding)
				&& name.equals(candidate.name) && isMethod == candidate.isMethod);

		if (isEqual) {
			if (qualifier != null && candidate.qualifier != null) {
				return qualifier.equals(candidate.qualifier);
			} else {
				return (qualifier == candidate.qualifier);
			}
		}

		return false;
	}
}
