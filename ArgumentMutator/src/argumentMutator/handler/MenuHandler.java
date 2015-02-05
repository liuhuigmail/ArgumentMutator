package argumentMutator.handler;


import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import argumentMutator.excelIO.ExcelIO;
import argumentMutator.parser.MutationParser;
import argumentMutator.utils.ProjectUtil;

public class MenuHandler extends AbstractHandler {

	public MenuHandler() {
	}

	public Object execute(ExecutionEvent event) throws ExecutionException {
		String commandID = event.getCommand().getId();
		handleCommand(commandID);
		return null;
	}

	private void handleCommand(String commandID) {
		if (commandID.equals("argumentMutator.command.mutateArguments")) {
			mutateProject();
		}
		
	}


	private void mutateProject() {
		IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		IProject project = ProjectUtil.getProject(window);
		
		MutationParser mutationParser = new MutationParser(project);
		mutationParser.parse();	
		
		ExcelIO.writeMutationResults(mutationParser.results);
				
	}
}
