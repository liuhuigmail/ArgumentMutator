package argumentMutator.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;

import argumentMutator.ast.model.VariableModel;

public class MethodUtil {

	public static List<VariableModel> getParameters(
			MethodInvocation methodInvocation) {
		return getParameters(methodInvocation.resolveMethodBinding());
	}

	public static List<VariableModel> getParameters(
			MethodDeclaration methodDeclaration) {
		return getParameters(methodDeclaration.resolveBinding());
	}

	public static List<VariableModel> getArguments(
			MethodInvocation methodInvocation) {
		@SuppressWarnings("unchecked")
		List<Expression> arguments = methodInvocation.arguments();

		List<VariableModel> variables = new ArrayList<VariableModel>();
		for (Expression argument : arguments) {
			int x=argument.getNodeType();
			if (x==ASTNode.METHOD_INVOCATION )
				x++;
			VariableModel variable = new VariableModel(argument);
			variables.add(variable);
		}

		return variables;
	}

	private static List<VariableModel> getParameters(
			IMethodBinding methodBinding) {
		List<VariableModel> variables = new ArrayList<VariableModel>();
		if (methodBinding == null) {
			return variables;
		}

		IMethod method = (IMethod) methodBinding.getJavaElement();
		if (method == null) {
			return variables;
		}

		String[] names = null;
		try {
			names = method.getParameterNames();
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return variables;
		}

		if (names == null || names.length == 0) {
			return variables;
		}

		ITypeBinding[] typeBindings = methodBinding.getParameterTypes();
		if (typeBindings == null || typeBindings.length != names.length) {
			return variables;
		}

		for (int index = 0; index < names.length; index++) {
			VariableModel variable = new VariableModel();
			variable.name = names[index];
			variable.typeBinding = typeBindings[index];
			variables.add(variable);
		}

		return variables;
	}

	@SuppressWarnings("unchecked")
	public static void replaceArgument(MethodInvocation methodInvocation,
			int index, Expression expression) {
		methodInvocation.arguments().remove(index);
		methodInvocation.arguments().add(index, expression);
	}
}
