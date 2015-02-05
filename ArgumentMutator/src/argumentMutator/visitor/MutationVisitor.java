package argumentMutator.visitor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import argumentMutator.model.CandidateModel;
import argumentMutator.model.ResultModel;
import argumentMutator.model.TypeUtil;
import argumentMutator.mutation.MethodMutation;

public class MutationVisitor extends ASTVisitor {

	private List<CandidateModel> members = null;
	public List<ResultModel> results;
	
	public String visitingTypeName = null;

	public MutationVisitor() {
		results = new ArrayList<ResultModel>();
	}

	@Override
	public boolean visit(AnonymousClassDeclaration node) {
		return false;
	}

	@Override
	public boolean visit(TypeDeclaration node) {
		if (visitingTypeName != null) {
			return false;
		}
		int modifiers = node.getModifiers();
		if (Modifier.isPublic(modifiers) && !Modifier.isStatic(modifiers)) {
			ITypeBinding typeBinding = node.resolveBinding();
			if (typeBinding != null) {
				visitingTypeName = node.getName().toString();
				members = new ArrayList<CandidateModel>();
				members.addAll(TypeUtil.getFields(typeBinding));
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void endVisit(TypeDeclaration node) {
		if (visitingTypeName != null
				&& visitingTypeName.equals(node.getName().toString())) {
			visitingTypeName = null;
		}
	}

	@Override
	public boolean visit(MethodInvocation node) {

		String expression = node.toString();
		MethodMutation mutation = new MethodMutation(node, members);
		String mutationExp = mutation.mutate();

		if (mutationExp != null) {
			ResultModel result = new ResultModel();
			result.expression = expression;
			result.mutation = mutationExp;
			results.add(result);
		}

		return true;
	}

	@Override
	public boolean visit(MethodDeclaration node) {
		int modifiers = node.getModifiers();
		return !Modifier.isStatic(modifiers);
	}
}
