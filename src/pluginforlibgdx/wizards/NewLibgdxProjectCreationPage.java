package pluginforlibgdx.wizards;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.internal.ui.refactoring.MessageWizardPage;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (mpe).
 */

public class NewLibgdxProjectCreationPage extends WizardPage {
	private static String WIZARD_PAGE_NAME = "libgdxProjctCreationWizardPage";
	private static String WIZARD_PAGE_TITLE = "Create new libgdx project";
	private static String WIZARD_PAGE_DESCRIPTION = "This wizard creates new libgdx project.";
	
	private static int NUMBER_OF_INPUT_FIELD = 6;
	
	private static String LABEL_PROJECT_NAME = "Project Name";
	private static String LABEL_PROJECT_MAIN_PACKAGE = "Package";
	private static String LABEL_PROJECT_MAIN_CLASS = "Game class";
	private static String LABEL_PROJECT_DESTINATION_FOLDER = "Destination";
	private static String LABEL_BUTTON_BROWSE = "Browse";
	private static String LABEL_DESKTOP_PROJECT = "Desktop project";
	
	private String DEFAULT_PROJECT_NAME = "NewLibgdxGameProject";
	private String DEFAULT_PROJECT_MAIN_PACKAGE="your.libgdx.project";
	private String DEFAULT_PROJECT_MAIN_CLASS = "GameClass";
	private String DEFAULT_PROJECT_DESTIONATION_FOLDER = "/home/superman/PluginExample";	
	
	private boolean isCreateDesktopProject = false;
	private Text textProjectNameContainer;
	private Text textProjectMainPackageContainer;
	private Text textProjectMainClassContainer;
	private Text textProjectDestinationFolder;
	private Button checkboxDesktopVersion;

	private ISelection selection;

	/**
	 * Constructor for NewLibgdxProjectCreationWizardPage.
	 * 
	 * @param pageName
	 */
	public NewLibgdxProjectCreationPage(ISelection selection) {
		super(WIZARD_PAGE_NAME);
		setTitle(WIZARD_PAGE_TITLE);
		setDescription(WIZARD_PAGE_DESCRIPTION);
		
		this.selection = selection;
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		
		GridLayout layout = new GridLayout(NUMBER_OF_INPUT_FIELD, false);
		layout.numColumns = 3;
		layout.verticalSpacing = 9;
		container.setLayoutData(new GridData(SWT.FILL));
		container.setLayout(layout);
		
		createNameInputText(container);
		createMainPackageInputText(container);
		createMainClassInputText(container);
		createDestinationFolderInputText(container);
		createDesktopVersionCheckbox(container);

		initialize();
		dialogChanged();
		setControl(container);
	}
	
	
	/***
	 * Create name input container include project name label and project name's inputText element
	 * @param container
	 */
	private void createNameInputText(Composite container){
		Label label = new Label(container, SWT.NULL);
		label.setText(LABEL_PROJECT_NAME);
		
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		
		textProjectNameContainer = new Text(container, SWT.BORDER | SWT.SINGLE);
		textProjectNameContainer.setLayoutData(gd);
		textProjectNameContainer.setText(DEFAULT_PROJECT_NAME);
		textProjectNameContainer.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		
		label = new Label(container, SWT.NULL);
	}
	
	/**
	 * Create main package name input container include package name label and project main package name's inputText element
	 * @param container
	 */
	private void createMainPackageInputText(Composite container){
		Label label = new Label(container, SWT.NULL);
		label.setText(LABEL_PROJECT_MAIN_PACKAGE);

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		
		textProjectMainPackageContainer = new Text(container, SWT.BORDER | SWT.SINGLE);
		textProjectMainPackageContainer.setLayoutData(gd);
		textProjectMainPackageContainer.setText(DEFAULT_PROJECT_MAIN_PACKAGE);
		textProjectMainPackageContainer.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		
		label = new Label(container, SWT.NULL);
	}
	
	
	/**
	 * 
	 * @param container
	 */
	private void createMainClassInputText(Composite container){
		Label label = new Label(container, SWT.NULL);
		label.setText(LABEL_PROJECT_MAIN_CLASS);
		
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		
		textProjectMainClassContainer = new Text(container, SWT.BORDER | SWT.SINGLE);
		textProjectMainClassContainer.setLayoutData(gd);
		textProjectMainClassContainer.setText(DEFAULT_PROJECT_MAIN_CLASS);
		textProjectMainClassContainer.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		
		label = new Label(container, SWT.NULL);
	}
	
	/**
	 * 
	 * @param container
	 */
	private void createDestinationFolderInputText(Composite container){
		Label label = new Label(container, SWT.NULL);
		label.setText(LABEL_PROJECT_DESTINATION_FOLDER);
		
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		
		textProjectDestinationFolder = new Text(container, SWT.BORDER | SWT.SINGLE);
		textProjectDestinationFolder.setLayoutData(gd);
		textProjectDestinationFolder.setText(DEFAULT_PROJECT_DESTIONATION_FOLDER);
		textProjectDestinationFolder.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		
		Button button = new Button(container, SWT.PUSH);
		button.setText(LABEL_BUTTON_BROWSE);
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleBrowse();
			}
		});

	}
	
	/**
	 * 
	 * @param container
	 */
	private void createDesktopVersionCheckbox(Composite container){
		Label label = new Label(container, SWT.NULL);
		label.setText(LABEL_DESKTOP_PROJECT);
		
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		
		checkboxDesktopVersion = new Button(container, SWT.CHECK);
		checkboxDesktopVersion.setLayoutData(gd);
		checkboxDesktopVersion.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				super.widgetSelected(e);
				isCreateDesktopProject = checkboxDesktopVersion.getSelection();
			}
		});
	}

	/**
	 * Tests if the current workbench selection is a suitable container to use.
	 */

	private void initialize() {
		if (selection != null && selection.isEmpty() == false
				&& selection instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) selection;
			if (ssel.size() > 1)
				return;
			Object obj = ssel.getFirstElement();
			if (obj instanceof IResource) {
				IContainer container;
				if (obj instanceof IContainer)
					container = (IContainer) obj;
				else
					container = ((IResource) obj).getParent();
			}
		}
	}

	/**
	 * Uses the standard container selection dialog to choose the new value for
	 * the container field.
	 */

	private void handleBrowse() {
		ContainerSelectionDialog dialog = new ContainerSelectionDialog(
				getShell(), ResourcesPlugin.getWorkspace().getRoot(), false,
				"Select new file container");
		if (dialog.open() == ContainerSelectionDialog.OK) {
			Object[] result = dialog.getResult();
			if (result.length == 1) {
//				containerText.setText(((Path) result[0]).toString());
			}
		}
	}

	/**
	 * Ensures that both text fields are set.
	 */

	private void dialogChanged() {
		if (isValidProjectName() && isValidProjectMainPackage() && isValidProjectMainClass() && isValidProjectDestinationFolder())
			updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}
	
	/**
	 * Check inputed project name
	 */
	boolean isValidProjectName(){
		String projectName = getProjectName();
		
		if (projectName.length() == 0){
			updateStatus(Message.PROJECT_NAME_IS_NULL);
			return false;
		}
		
		if (projectName.contains("'.,:?;")){
			updateStatus(Message.PROJECT_NAME_CONTAINS_INVALID_CHARACTER);
			return false;
		}
		
		return true;
	}
	
	boolean isValidProjectMainPackage(){
		String projectMainPackage = getProjectMainPackage();
		
		if  (projectMainPackage.length() == 0){
			updateStatus(Message.PROJECT_MAIN_PACKAGE_IS_NULL);
			return false;
		}
		
		if (projectMainPackage.contains(",?:;")){
			updateStatus(Message.PROJECT_MAIN_PACKAGE_CONTAINS_INVALID_CHARACTER);
			return false;
		}
		
		if (projectMainPackage.endsWith(".")){
			updateStatus(Message.PROJECT_MAIN_PACKAGE_ENDS_BY_DOT_CHACRACTER);
			return false;
		}
		return true;
	}
	
	boolean isValidProjectMainClass(){
		String projectMainClass = getProjectMainClass();
		
		if (projectMainClass.length() == 0){
			updateStatus(Message.PROJECT_MAIN_CLASS_IS_NULL);
			return false;
		}
		
		if (projectMainClass.contains("?:.,")){
			updateStatus(Message.PROJECT_MAIN_CLASS_CONTAINS_INVALID_CHARACTER);
			return false;
		}
		
		if (!Character.isUpperCase(projectMainClass.charAt(0))){
			updateStatus(Message.PROJECT_MAIN_CLASS_BEGINS_WITH_LOWERCASE_CHARACTER);
			return false;
		}
		return true;
	}
	
	boolean isValidProjectDestinationFolder(){
		String projectDestinationFolder = getProjectDestinationFolder();
		
		if (projectDestinationFolder.length() == 0){
			updateStatus(Message.PROJECT_DESTINATION_FOLDER_IS_NULL);
			return false;
		}
		
		// TODO
		// Check destination folder exist
		// TODO
		
		return true;
	}
	
	/**
	 * GET method
	 */
	public String getProjectName(){
		return textProjectNameContainer.getText();
	}
	
	public String getProjectMainPackage(){
		return textProjectMainPackageContainer.getText();
	}
	
	public String getProjectMainClass(){
		return textProjectMainClassContainer.getText();
	}
	
	public String getProjectDestinationFolder(){
		return textProjectDestinationFolder.getText();
	}
}