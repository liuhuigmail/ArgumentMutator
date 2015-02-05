package argumentMutator.utils;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchWindow;

public class ProjectUtil {

	public static IProject getProject(IWorkbenchWindow window) {
		IProject project = null;

        ISelectionService selectionService = window.getSelectionService();
        ISelection selection = selectionService.getSelection();

        if (selection instanceof IStructuredSelection) {
                Object element = ((IStructuredSelection) selection)
                                .getFirstElement();
                if (element instanceof IResource) {
                        project = ((IResource) element).getProject();
                } else if (element instanceof IJavaElement) {
                        IJavaElement javaElement = (IJavaElement) element;
                        project = javaElement.getJavaProject().getProject();
                }
        }

        if (project == null) {
                IEditorPart editorPart = window.getActivePage().getActiveEditor();
                if (editorPart != null) {
                        IEditorInput input = editorPart.getEditorInput();
                        if (input instanceof IFileEditorInput) {
                                project = ((IFileEditorInput) input).getFile().getProject();
                        }
                }
        }

        return project;
	}

}
