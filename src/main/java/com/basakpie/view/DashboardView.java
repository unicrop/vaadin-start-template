package com.basakpie.view;

import com.basakpie.security.UserRepository;
import com.basakpie.sidebar.Sections;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.*;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;

import javax.annotation.PostConstruct;

/**
 * Created by basakpie on 2017. 5. 11..
 */
@SpringView(name = DashboardView.VIEW_NAME)
@SideBarItem(sectionId = Sections.NO_GROUP, caption = "Dashboard", order = 1)
@VaadinFontIcon(VaadinIcons.DASHBOARD)
public class DashboardView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "";

    @Autowired
    UserRepository userRepository;

    @PostConstruct
    public void init() {
        addComponent(new Label("Hello, this is dashboard view."));
        addComponent(loadingIndicators());
        addComponent(notifications());
        addComponent(windows());
        addComponent(tooltips());
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

    Panel loadingIndicators() {
        Panel p = new Panel("Loading Indicator");
        VerticalLayout content = new VerticalLayout();
        p.setContent(content);
        content.setSpacing(true);
        content.addComponent(new Label(
                "You can test the loading indicator by pressing the buttons."));

        CssLayout group = new CssLayout();
        group.setCaption("Show the loading indicator for…");
        group.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        content.addComponent(group);
        Button loading = new Button("0.8");
        loading.addClickListener(event -> {
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
            }
        });
        group.addComponent(loading);

        Button delay = new Button("3");
        delay.addClickListener(event -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
            }
        });
        group.addComponent(delay);

        Button wait = new Button("15");
        wait.addClickListener(event -> {
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
            }
        });
        wait.addStyleName("last");
        group.addComponent(wait);
        Label label = new Label("&nbsp;&nbsp; seconds", ContentMode.HTML);
        label.setSizeUndefined();
        group.addComponent(label);

        Label spinnerDesc = new Label(
                "The theme also provides a mixin that you can use to include a spinner anywhere in your application. Below is a Label with a custom style name, for which the spinner mixin is added.");
        spinnerDesc.addStyleName(ValoTheme.LABEL_SMALL);
        spinnerDesc.setCaption("Spinner");
        content.addComponent(spinnerDesc);

        return p;
    }

    Panel notifications() {
        Panel p = new Panel("Notifications");
        VerticalLayout content = new VerticalLayout() {
            Notification notification = new Notification("");
            TextField title = new TextField("Title");
            TextArea description = new TextArea("Description");
            MenuBar style = new MenuBar();
            MenuBar type = new MenuBar();
            String typeString = "";
            String styleString = "";
            TextField delay = new TextField();
            {
                setSpacing(true);
                setMargin(true);

                title.setPlaceholder("Title for the notification");
                title.addValueChangeListener(event -> {
                    if (title.getValue() == null
                            || title.getValue().length() == 0) {
                        notification.setCaption(null);
                    } else {
                        notification.setCaption(title.getValue());
                    }
                });
                title.setValue("Notification Title");
                title.setWidth("100%");
                addComponent(title);

                description.setPlaceholder("Description for the notification");
                description.addStyleName(ValoTheme.TEXTAREA_SMALL);
                description.addValueChangeListener(event -> {
                    if (description.getValue() == null
                            || description.getValue().length() == 0) {
                        notification.setDescription(null);
                    } else {
                        notification.setDescription(description.getValue());
                    }
                });
                description
                        .setValue("A more informative message about what has happened. Nihil hic munitissimus habendi senatus locus, nihil horum? Inmensae subtilitatis, obscuris et malesuada fames. Hi omnes lingua, institutis, legibus inter se differunt.");
                description.setWidth("100%");
                addComponent(description);

                MenuBar.Command typeCommand = (MenuBar.Command) selectedItem -> {
                    if (selectedItem.getText().equals("Humanized")) {
                        typeString = "";
                        notification.setStyleName(styleString.trim());
                    } else {
                        typeString = selectedItem.getText().toLowerCase();
                        notification
                                .setStyleName((typeString + " " + styleString
                                        .trim()).trim());
                    }
                    for (MenuBar.MenuItem item : type.getItems()) {
                        item.setChecked(false);
                    }
                    selectedItem.setChecked(true);
                };

                type.setCaption("Type");
                MenuBar.MenuItem humanized = type.addItem("Humanized", typeCommand);
                humanized.setCheckable(true);
                humanized.setChecked(true);
                type.addItem("Tray", typeCommand).setCheckable(true);
                type.addItem("Warning", typeCommand).setCheckable(true);
                type.addItem("Error", typeCommand).setCheckable(true);
                type.addItem("System", typeCommand).setCheckable(true);
                addComponent(type);
                type.addStyleName(ValoTheme.MENUBAR_SMALL);

                MenuBar.Command styleCommand = (MenuBar.Command) selectedItem -> {
                    styleString = "";
                    for (MenuBar.MenuItem item : style.getItems()) {
                        if (item.isChecked()) {
                            styleString += " "
                                    + item.getText().toLowerCase();
                        }
                    }
                    if (styleString.trim().length() > 0) {
                        notification
                                .setStyleName((typeString + " " + styleString
                                        .trim()).trim());
                    } else if (typeString.length() > 0) {
                        notification.setStyleName(typeString.trim());
                    } else {
                        notification.setStyleName(null);
                    }
                };

                style.setCaption("Additional style");
                style.addItem("Dark", styleCommand).setCheckable(true);
                style.addItem("Success", styleCommand).setCheckable(true);
                style.addItem("Failure", styleCommand).setCheckable(true);
                style.addItem("Bar", styleCommand).setCheckable(true);
                style.addItem("Small", styleCommand).setCheckable(true);
                style.addItem("Closable", styleCommand).setCheckable(true);
                addComponent(style);
                style.addStyleName(ValoTheme.MENUBAR_SMALL);

                CssLayout group = new CssLayout();
                group.setCaption("Fade delay");
                group.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
                addComponent(group);

                delay.setPlaceholder("Infinite");
                delay.addStyleName(ValoTheme.TEXTFIELD_ALIGN_RIGHT);
                delay.addStyleName(ValoTheme.TEXTFIELD_SMALL);
                delay.setWidth("7em");
                delay.addValueChangeListener(event -> {
                    try {
                        notification.setDelayMsec(Integer.parseInt(delay
                                .getValue()));
                    } catch (Exception e) {
                        notification.setDelayMsec(-1);
                        delay.setValue("");
                    }
                });
                delay.setValue("1000");
                group.addComponent(delay);

                Button clear = new Button(null, (Button.ClickListener) event -> delay.setValue(""));
                clear.setIcon(VaadinIcons.TIMER);
                clear.addStyleName("last");
                clear.addStyleName(ValoTheme.BUTTON_SMALL);
                clear.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
                group.addComponent(clear);
                group.addComponent(new Label("&nbsp; msec", ContentMode.HTML));

                GridLayout grid = new GridLayout(3, 3);
                grid.setCaption("Show in position");
                addComponent(grid);
                grid.setSpacing(true);

                Button pos = new Button("", (Button.ClickListener) event -> {
                    notification.setPosition(Position.TOP_LEFT);
                    notification.show(Page.getCurrent());
                });
                pos.addStyleName(ValoTheme.BUTTON_SMALL);
                grid.addComponent(pos);

                pos = new Button("", (Button.ClickListener) event -> {
                    notification.setPosition(Position.TOP_CENTER);
                    notification.show(Page.getCurrent());
                });
                pos.addStyleName(ValoTheme.BUTTON_SMALL);
                grid.addComponent(pos);

                pos = new Button("", (Button.ClickListener) event -> {
                    notification.setPosition(Position.TOP_RIGHT);
                    notification.show(Page.getCurrent());
                });
                pos.addStyleName(ValoTheme.BUTTON_SMALL);
                grid.addComponent(pos);

                pos = new Button("", (Button.ClickListener) event -> {
                    notification.setPosition(Position.MIDDLE_LEFT);
                    notification.show(Page.getCurrent());
                });
                pos.addStyleName(ValoTheme.BUTTON_SMALL);
                grid.addComponent(pos);

                pos = new Button("", (Button.ClickListener) event -> {
                    notification.setPosition(Position.MIDDLE_CENTER);
                    notification.show(Page.getCurrent());
                });
                pos.addStyleName(ValoTheme.BUTTON_SMALL);
                grid.addComponent(pos);

                pos = new Button("", (Button.ClickListener) event -> {
                    notification.setPosition(Position.MIDDLE_RIGHT);
                    notification.show(Page.getCurrent());
                });
                pos.addStyleName(ValoTheme.BUTTON_SMALL);
                grid.addComponent(pos);

                pos = new Button("", (Button.ClickListener) event -> {
                    notification.setPosition(Position.BOTTOM_LEFT);
                    notification.show(Page.getCurrent());
                });
                pos.addStyleName(ValoTheme.BUTTON_SMALL);
                grid.addComponent(pos);

                pos = new Button("", (Button.ClickListener) event -> {
                    notification.setPosition(Position.BOTTOM_CENTER);
                    notification.show(Page.getCurrent());
                });
                pos.addStyleName(ValoTheme.BUTTON_SMALL);
                grid.addComponent(pos);

                pos = new Button("", (Button.ClickListener) event -> {
                    notification.setPosition(Position.BOTTOM_RIGHT);
                    notification.show(Page.getCurrent());
                });
                pos.addStyleName(ValoTheme.BUTTON_SMALL);
                grid.addComponent(pos);

            }
        };
        p.setContent(content);

        return p;
    }

    Panel tooltips() {
        Panel p = new Panel("Tooltips");
        HorizontalLayout content = new HorizontalLayout() {
            {
                setSpacing(true);
                setMargin(true);
                addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);

                addComponent(new Label(
                        "Try out different tooltips/descriptions by hovering over the labels."));

                Label label = new Label("Simple");
                label.addStyleName(ValoTheme.LABEL_BOLD);
                label.setDescription("Simple tooltip message");
                addComponent(label);

                label = new Label("Long");
                label.addStyleName(ValoTheme.LABEL_BOLD);
                label.setDescription("Long tooltip message. Inmensae subtilitatis, obscuris et malesuada fames. Salutantibus vitae elit libero, a pharetra augue.");
                addComponent(label);

                label = new Label("HTML tooltip");
                label.addStyleName(ValoTheme.LABEL_BOLD);
                label.setDescription("<div><h1>Ut enim ad minim veniam, quis nostrud exercitation</h1><p><span>Morbi fringilla convallis sapien, id pulvinar odio volutpat.</span> <span>Vivamus sagittis lacus vel augue laoreet rutrum faucibus.</span> <span>Donec sed odio operae, eu vulputate felis rhoncus.</span> <span>At nos hinc posthac, sitientis piros Afros.</span> <span>Tu quoque, Brute, fili mi, nihil timor populi, nihil!</span></p><p><span>Gallia est omnis divisa in partes tres, quarum.</span> <span>Praeterea iter est quasdam res quas ex communi.</span> <span>Cum ceteris in veneratione tui montes, nascetur mus.</span> <span>Quam temere in vitiis, legem sancimus haerentia.</span> <span>Idque Caesaris facere voluntate liceret: sese habere.</span></p></div>");
                addComponent(label);

                label = new Label("With an error message");
                label.addStyleName(ValoTheme.LABEL_BOLD);
                label.setDescription("Simple tooltip message");
                label.setComponentError(new UserError(
                        "Something terrible has happened"));
                addComponent(label);

                label = new Label("With a long error message");
                label.addStyleName(ValoTheme.LABEL_BOLD);
                label.setDescription("Simple tooltip message");
                label.setComponentError(new UserError(
                        "<h2>Contra legem facit qui id facit quod lex prohibet <span>Tityre, tu patulae recubans sub tegmine fagi  dolor.</span> <span>Tityre, tu patulae recubans sub tegmine fagi  dolor.</span> <span>Prima luce, cum quibus mons aliud  consensu ab eo.</span> <span>Quid securi etiam tamquam eu fugiat nulla pariatur.</span> <span>Fabio vel iudice vincam, sunt in culpa qui officia.</span> <span>Nihil hic munitissimus habendi senatus locus, nihil horum?</span></p><p><span>Plura mihi bona sunt, inclinet, amari petere vellent.</span> <span>Integer legentibus erat a ante historiarum dapibus.</span> <span>Quam diu etiam furor iste tuus nos eludet?</span> <span>Nec dubitamus multa iter quae et nos invenerat.</span> <span>Quisque ut dolor gravida, placerat libero vel, euismod.</span> <span>Quae vero auctorem tractata ab fiducia dicuntur.</span></h2>",
                        AbstractErrorMessage.ContentMode.HTML,
                        ErrorMessage.ErrorLevel.CRITICAL));
                addComponent(label);

                label = new Label("Error message only");
                label.addStyleName(ValoTheme.LABEL_BOLD);
                label.setComponentError(new UserError(
                        "Something terrible has happened"));
                addComponent(label);
            }
        };
        p.setContent(content);
        return p;

    }

    Panel windows() {
        Panel p = new Panel("Dialogs");
        VerticalLayout content = new VerticalLayout() {
            final Window win = new Window("Window Caption");
            String prevHeight = "300px";
            boolean footerVisible = true;
            boolean autoHeight = false;
            boolean tabsVisible = false;
            boolean toolbarVisible = false;
            boolean footerToolbar = false;
            boolean toolbarLayout = false;
            String toolbarStyle = null;

            VerticalLayout windowContent() {
                VerticalLayout root = new VerticalLayout();

                Component content = null;

                if (tabsVisible) {
                    TabSheet tabs = new TabSheet();
                    tabs.setSizeFull();
                    VerticalLayout l = new VerticalLayout();
                    l.addComponent(new Label(
                            "<h2>Subtitle</h2><p>Normal type for plain text. Etiam at risus et justo dignissim congue. Phasellus laoreet lorem vel dolor tempus vehicula.</p><p>Quisque ut dolor gravida, placerat libero vel, euismod. Etiam habebis sem dicantur magna mollis euismod. Nihil hic munitissimus habendi senatus locus, nihil horum? Curabitur est gravida et libero vitae dictum. Ullamco laboris nisi ut aliquid ex ea commodi consequat. Morbi odio eros, volutpat ut pharetra vitae, lobortis sed nibh.</p>",
                            ContentMode.HTML));
                    l.setMargin(true);
                    tabs.addTab(l, "Selected");
                    tabs.addTab(new Label("&nbsp;", ContentMode.HTML),
                            "Another");
                    tabs.addTab(new Label("&nbsp;", ContentMode.HTML),
                            "One more");
                    tabs.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
                    tabs.addSelectedTabChangeListener((TabSheet.SelectedTabChangeListener) event -> {
                        try {
                            Thread.sleep(600);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
                    content = tabs;
                } else if (!autoHeight) {
                    Panel p = new Panel();
                    p.setSizeFull();
                    p.addStyleName(ValoTheme.PANEL_BORDERLESS);
                    if (!toolbarVisible || !toolbarLayout) {
                        p.addStyleName(ValoTheme.PANEL_SCROLL_INDICATOR);
                    }
                    VerticalLayout l = new VerticalLayout();
                    l.addComponent(new Label(
                            "<h2>Subtitle</h2><p>Normal type for plain text. Etiam at risus et justo dignissim congue. Phasellus laoreet lorem vel dolor tempus vehicula.</p><p>Quisque ut dolor gravida, placerat libero vel, euismod. Etiam habebis sem dicantur magna mollis euismod. Nihil hic munitissimus habendi senatus locus, nihil horum? Curabitur est gravida et libero vitae dictum. Ullamco laboris nisi ut aliquid ex ea commodi consequat. Morbi odio eros, volutpat ut pharetra vitae, lobortis sed nibh.</p>",
                            ContentMode.HTML));
                    l.setMargin(true);
                    p.setContent(l);
                    content = p;
                } else {
                    content = new Label(
                            "<h2>Subtitle</h2><p>Normal type for plain text. Etiam at risus et justo dignissim congue. Phasellus laoreet lorem vel dolor tempus vehicula.</p><p>Quisque ut dolor gravida, placerat libero vel, euismod. Etiam habebis sem dicantur magna mollis euismod. Nihil hic munitissimus habendi senatus locus, nihil horum? Curabitur est gravida et libero vitae dictum. Ullamco laboris nisi ut aliquid ex ea commodi consequat. Morbi odio eros, volutpat ut pharetra vitae, lobortis sed nibh.</p>",
                            ContentMode.HTML);
                    root.setMargin(true);
                }

                root.addComponent(content);

                if (footerVisible) {
                    HorizontalLayout footer = new HorizontalLayout();
                    footer.setWidth("100%");
                    footer.setSpacing(true);
                    footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);

                    Label footerText = new Label("Footer text");
                    footerText.setSizeUndefined();

                    Button ok = new Button("OK");
                    ok.addStyleName(ValoTheme.BUTTON_PRIMARY);

                    Button cancel = new Button("Cancel");

                    footer.addComponents(footerText, ok, cancel);
                    footer.setExpandRatio(footerText, 1);

                    root.addComponent(footer);
                }

                if (!autoHeight) {
                    root.setSizeFull();
                    root.setExpandRatio(content, 1);
                }

                return root;
            }

            {
                setSpacing(true);
                setMargin(true);
                win.setWidth("380px");
                win.setHeight(prevHeight);
                win.setClosable(true);
                win.setResizable(true);
                win.setContent(windowContent());

                MenuBar.Command optionsCommand = (MenuBar.Command) selectedItem -> {
                    if (selectedItem.getText().equals("Footer")) {
                        footerVisible = selectedItem.isChecked();
                    }
                    if (selectedItem.getText().equals("Auto Height")) {
                        autoHeight = selectedItem.isChecked();
                        if (!autoHeight) {
                            win.setHeight(prevHeight);
                        } else {
                            prevHeight = win.getHeight()
                                    + win.getHeightUnits().toString();
                            win.setHeight(null);
                        }
                    }
                    if (selectedItem.getText().equals("Tabs")) {
                        tabsVisible = selectedItem.isChecked();
                    }

                    if (selectedItem.getText().equals("Top")) {
                        toolbarVisible = selectedItem.isChecked();
                    }

                    if (selectedItem.getText().equals("Footer")) {
                        footerToolbar = selectedItem.isChecked();
                    }

                    if (selectedItem.getText().equals("Top layout")) {
                        toolbarLayout = selectedItem.isChecked();
                    }

                    if (selectedItem.getText().equals("Borderless")) {
                        toolbarStyle = selectedItem.isChecked() ? "borderless"
                                : null;
                    }

                    win.setContent(windowContent());
                };

                MenuBar options = new MenuBar();
                options.setCaption("Content");
                options.addItem("Auto Height", optionsCommand).setCheckable(
                        true);
                options.addItem("Tabs", optionsCommand).setCheckable(true);
                MenuBar.MenuItem option = options.addItem("Footer", optionsCommand);
                option.setCheckable(true);
                option.setChecked(true);
                options.addStyleName(ValoTheme.MENUBAR_SMALL);
                addComponent(options);

                options = new MenuBar();
                options.setCaption("Toolbars");
                options.addItem("Footer", optionsCommand).setCheckable(true);
                options.addItem("Top", optionsCommand).setCheckable(true);
                options.addItem("Top layout", optionsCommand)
                        .setCheckable(true);
                options.addItem("Borderless", optionsCommand)
                        .setCheckable(true);
                options.addStyleName(ValoTheme.MENUBAR_SMALL);
                addComponent(options);

                MenuBar.Command optionsCommand2 = (MenuBar.Command) selectedItem -> {
                    if (selectedItem.getText().equals("Caption")) {
                        win.setCaption(selectedItem.isChecked() ? "Window Caption"
                                : null);
                    } else if (selectedItem.getText().equals("Closable")) {
                        win.setClosable(selectedItem.isChecked());
                    } else if (selectedItem.getText().equals("Resizable")) {
                        win.setResizable(selectedItem.isChecked());
                    } else if (selectedItem.getText().equals("Modal")) {
                        win.setModal(selectedItem.isChecked());
                    }
                };

                options = new MenuBar();
                options.setCaption("Options");
                MenuBar.MenuItem caption = options.addItem("Caption", optionsCommand2);
                caption.setCheckable(true);
                caption.setChecked(true);
                options.addItem("Closable", optionsCommand2).setCheckable(true);
                options.addItem("Resizable", optionsCommand2)
                        .setCheckable(true);
                options.addItem("Modal", optionsCommand2).setCheckable(true);
                options.addStyleName(ValoTheme.MENUBAR_SMALL);
                addComponent(options);

                final Button show = new Button("Open Window",
                        (Button.ClickListener) event -> {
                            getUI().addWindow(win);
                            win.center();
                            win.focus();
                            event.getButton().setEnabled(false);
                        });
                show.addStyleName(ValoTheme.BUTTON_PRIMARY);
                addComponent(show);

                final CheckBox hidden = new CheckBox("Hidden");
                hidden.addValueChangeListener(event -> {
                    win.setVisible(!hidden.getValue());
                });
                addComponent(hidden);

                win.addCloseListener((Window.CloseListener) e -> show.setEnabled(true));
            }
        };
        p.setContent(content);
        return p;

    }
}

