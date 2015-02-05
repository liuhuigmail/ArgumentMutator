package argumentMutator.parser;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class CommonParser {

	protected IJavaProject project = null;

	public CommonParser(IProject project) {
		this.project = JavaCore.create(project);
	}

	protected List<ICompilationUnit> getAllCompilationUnits() {
		List<ICompilationUnit> allCompilationUnits = new ArrayList<ICompilationUnit>();
		if (project == null) {
			return allCompilationUnits;
		}

		IPackageFragment[] packageFragments = getPackageFragments(project);
		if (packageFragments == null) {
			return allCompilationUnits;
		}

		for (IPackageFragment packageFragment : packageFragments) {
			ICompilationUnit[] compilationUnits = getCompilationUnits(packageFragment);
			if (compilationUnits == null) {
				continue;
			}

			for (ICompilationUnit compilationUnit : compilationUnits) {
				allCompilationUnits.add(compilationUnit);
			}
		}
		return allCompilationUnits;
	}

	private IPackageFragment[] getPackageFragments(IJavaProject javaProject) {
		IPackageFragment[] packageFragments = null;

		try {
			packageFragments = javaProject.getPackageFragments();
		} catch (JavaModelException e) {
			return null;
		}

		return packageFragments;
	}

	private ICompilationUnit[] getCompilationUnits(
			IPackageFragment packageFragment) {
		try {
			if (packageFragment.getKind() != IPackageFragmentRoot.K_SOURCE) {
				return null;
			}
		} catch (JavaModelException e) {
			return null;
		}

		ICompilationUnit[] compilationUnits = null;

		try {
			compilationUnits = packageFragment.getCompilationUnits();
		} catch (JavaModelException e) {
			return null;
		}

		return compilationUnits;
	}
	
	protected CompilationUnit createCompilationUnit(ICompilationUnit compilationUnit) {
		ASTParser parser = ASTParser.newParser(AST.JLS4);
		parser.setProject(project);
		parser.setSource(compilationUnit);
		parser.setResolveBindings(true);
		CompilationUnit unit = (CompilationUnit) parser.createAST(null);
		return unit;
	}
}
