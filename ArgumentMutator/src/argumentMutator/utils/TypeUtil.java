package argumentMutator.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.Modifier;

import argumentMutator.ast.model.MethodModel;
import argumentMutator.ast.model.VariableModel;

public class TypeUtil {

	public static boolean isStaticField(ITypeBinding typeBinding, String name) {
		return false;
	}
	
	public static List<VariableModel> getFields(ITypeBinding typeBinding) {
		List<VariableModel> fields = new ArrayList<VariableModel>();
		
		IVariableBinding[] variableBindings = typeBinding.getDeclaredFields();
		if (variableBindings == null) {
			return fields;
		}
		
		for (IVariableBinding variableBinding : variableBindings) {
			int modifiers = variableBinding.getModifiers();
			if (Modifier.isStatic(modifiers)) {
				continue;
			}
			
			VariableModel field = new VariableModel(variableBinding);
			//if(field.isLegal()){
				field.externalAccessible = Modifier.isPublic(modifiers);
				fields.add(field);
			//}
		}
		
		return fields;
	}
	
	public static List<MethodModel> getMethods(ITypeBinding typeBinding) {
		List<MethodModel> methods = new ArrayList<MethodModel>();
		
		IMethodBinding[] methodBindings = typeBinding.getDeclaredMethods();
		if (methodBindings == null) {
			return methods;
		}
		
		for (IMethodBinding methodBinding : methodBindings) {
			int modifiers = methodBinding.getModifiers();
			if (Modifier.isStatic(modifiers)) {
				continue;
			}
			
			ITypeBinding[] typeBindings = methodBinding.getParameterTypes();
			if (typeBindings != null && typeBindings.length != 0) {
				continue;
			}
			
			MethodModel method = new MethodModel(methodBinding);
			if (method.isLegal()) {
				method.externalAccessible = Modifier.isPublic(modifiers);
				methods.add(method);
			}
		}
		
		return methods;
	}
}
