package argumentMutator.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.filebuffers.ITextFileBufferManager;
import org.eclipse.core.filebuffers.LocationKind;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.TextEdit;

import argumentMutator.model.ResultModel;
import argumentMutator.visitor.MutationVisitor;

public class MutationParser extends CommonParser {

	public List<ResultModel> results = null;

	public MutationParser(IProject project) {
		super(project);
		results = new ArrayList<ResultModel>();
	}

	public void parse() {
		List<ICompilationUnit> compilationUnits = getAllCompilationUnits();
		for (ICompilationUnit compilationUnit : compilationUnits) {
			mutation(compilationUnit);
		}
		
	}

	private void mutation(ICompilationUnit compilationUnit) {
		CompilationUnit unit = createCompilationUnit(compilationUnit);
		if (unit == null) {
			return;
		}

		unit.recordModifications();
		MutationVisitor visitor = new MutationVisitor();
		
		unit.accept(visitor);

		List<ResultModel> results = visitor.results;

		if (results.size() > 0) {
			writeBack(unit);
		}

		for (ResultModel result : results) {
			result.path = compilationUnit.getPath();
		}

		this.results.addAll(results);
	}

	private void writeBack(CompilationUnit unit) {
		ITextFileBufferManager manager = FileBuffers.getTextFileBufferManager();
		IPath path = unit.getJavaElement().getPath();

		IFile file = project.getProject().getFile(path);
		String encoding = null;
		try {
			encoding = file.getCharset();
		} catch (CoreException e1) {
		}
		try {
			manager.connect(path, LocationKind.NORMALIZE, null);
			ITextFileBuffer textFileBuffer = manager.getTextFileBuffer(path,
					LocationKind.NORMALIZE);
			IDocument document = textFileBuffer.getDocument();
			Map<String, String> options = null;
			if (encoding != null) {
				options = new HashMap<String, String>();
				options.put("org.eclipse.jdt.core.encoding", encoding);
			}
			TextEdit textEdit = unit.rewrite(document, options);
			textEdit.apply(document);
			textFileBuffer.commit(null, false);
		} catch (Exception e) {
		} finally {
			try {
				manager.disconnect(path, LocationKind.NORMALIZE, null);
			} catch (CoreException e) {
			}
		}
	}

	
}
