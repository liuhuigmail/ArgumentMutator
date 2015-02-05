package argumentMutator.ast.model;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.ThisExpression;

public class VariableModel extends CandidateModel {

	public VariableModel() {

	}

	public VariableModel(IVariableBinding variableBinding) {
		this.name = variableBinding.getName();
		this.typeBinding = variableBinding.getType();
		int modifiers = variableBinding.getModifiers();
		this.externalAccessible = Modifier.isPublic(modifiers);
	}

	public VariableModel(Expression expression) {
		if (expression instanceof SimpleName) {
			genVariableModel((SimpleName) expression);
		} else if (expression instanceof QualifiedName) {
			genVariableModel((QualifiedName) expression);
		} else if (expression instanceof ThisExpression) {
			genVariableModel((ThisExpression) expression);
		}
		
		else if(expression instanceof FieldAccess){
			genVariableModel((FieldAccess) expression);
		}
		
	}

	private void genVariableModel(SimpleName simpleName) {
		this.name = simpleName.toString();
		this.typeBinding = simpleName.resolveTypeBinding();
	}

	private void genVariableModel(QualifiedName qualifiedName) {
		Name qualifier = qualifiedName.getQualifier();
		SimpleName simpleName = qualifiedName.getName();

		if (qualifier == null || simpleName == null) {
			return;
		}

		genVariableModel(simpleName);
		this.qualifier = qualifier.toString();
		this.qualifierTypeBinding = qualifier.resolveTypeBinding();
	}

	private void genVariableModel(ThisExpression expression) {
		this.name = "this";
		this.typeBinding = expression.resolveTypeBinding();
	}
	
	private void genVariableModel(FieldAccess expression) {
		SimpleName simpleName = expression.getName();
		genVariableModel(simpleName);
		
	}
}
