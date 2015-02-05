package argumentMutator.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;


public class LocalUtil {

	public static List<VariableModel> getVariables(ASTNode node) {
		List<VariableModel> variables = new ArrayList<VariableModel>();

		while (node != null) {
			if (node instanceof MethodDeclaration) {
				MethodDeclaration methodDeclaration = (MethodDeclaration) node;
				variables.addAll(MethodUtil.getParameters(methodDeclaration));
				break;
			}
			if (node instanceof Statement && node.getParent() instanceof Block) {
				variables.addAll(getVariablesFromStatement((Statement) node));
			}
			node = node.getParent();
		}

		return variables;
	}

	private static List<VariableModel> getVariablesFromStatement(Statement statement) {
		List<VariableModel> variables = new ArrayList<VariableModel>();
		Block block = (Block) statement.getParent();
		@SuppressWarnings("unchecked")
		List<Statement> statements = block.statements();
		for (Statement siblingStatement : statements) {
			if (siblingStatement == statement) {
				break;
			}
			if (!(siblingStatement instanceof VariableDeclarationStatement)) {
				continue;
			}

			VariableDeclarationStatement variableStatement = (VariableDeclarationStatement) siblingStatement;
			Type type = variableStatement.getType();
			if (type == null) {
				continue;
			}

			ITypeBinding typeBinding = null;
			try {
				typeBinding = type.resolveBinding();
			} catch (Exception e) {
				continue;
			}
			@SuppressWarnings("unchecked")
			List<VariableDeclarationFragment> fragments = variableStatement
					.fragments();
			for (VariableDeclarationFragment fragment : fragments) {
				VariableModel variable = new VariableModel();
				variable.name = fragment.getName().toString();
				variable.typeBinding = typeBinding;
				variables.add(variable);
			}
		}

		return variables;
	}
}
