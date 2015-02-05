package argumentMutator.ast.model;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;

public class MethodModel extends CandidateModel {
	
	public MethodModel(MethodDeclaration methodDeclaration) {
		super(true);
		IMethodBinding methodBinding = methodDeclaration.resolveBinding();
		if (methodBinding != null) {
			genFromMethodBinding(methodBinding);
		}
	}
	
	public MethodModel(MethodInvocation methodInvocation) {
		super(true);
		IMethodBinding methodBinding = methodInvocation.resolveMethodBinding();
		if (methodBinding != null) {
			genFromMethodBinding(methodBinding);
			Expression expression = methodInvocation.getExpression();
			if (expression != null) {
				this.qualifier = expression.toString();
				this.qualifierTypeBinding = expression.resolveTypeBinding();
			}
		}
	}
	
	public MethodModel(IMethodBinding methodBinding) {
		super(true);
		genFromMethodBinding(methodBinding);
	}
	
	public void genFromMethodBinding(IMethodBinding methodBinding) {
		this.name = methodBinding.getName();
		this.typeBinding = methodBinding.getReturnType();
		int modifiers = methodBinding.getModifiers();
		this.externalAccessible = Modifier.isStatic(modifiers);
	}
}
