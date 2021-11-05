//  WalkThroughViewController.swift
//  Workspace-ONE-SDK-iOS-Swift-Sample
//
//  Copyright 2021 VMware, Inc.
//  SPDX-License-Identifier: BSD-2-Clause
//


import UIKit

class WalkThroughViewController: UIViewController {

    private var pageViewController: UIPageViewController!
    internal var pageDescription: [String] = []
    internal var pageMedia: [String] = []

    override func viewDidLoad() {
        super.viewDidLoad()

        // Setting the Dot based slider
        let pageNavigationController = UIPageControl.appearance()
        pageNavigationController.pageIndicatorTintColor = .lightGray
        pageNavigationController.currentPageIndicatorTintColor = .black
        pageNavigationController.backgroundColor = .white

        // Instantiating the pageViewController
        self.pageViewController = self.storyboard?.instantiateViewController(withIdentifier: "pageVC") as? UIPageViewController

        // Attaching the datasource as self
        self.pageViewController.dataSource = self

        // This is my custom method which returns references to ContentViewController
        // Getting the first ContentViewController
        let startVC = self.viewControllerAtIndex(0) as TutorialViewController

        let viewControllers: [UIViewController] = [startVC]

        // Setting the viewControllers array as  pageViewController sliders
        self.pageViewController.setViewControllers(viewControllers, direction: .forward, animated: true, completion: nil)

        // Creating a frame in pageViewController
        self.pageViewController.view.frame = CGRect(x: 0, y: 30, width: self.view.frame.width, height: self.view.frame.size.height - 60)

        // Adding the pageViewController as my self child
        self.addChild(self.pageViewController)
        self.view.addSubview(self.pageViewController.view)
        self.pageViewController.didMove(toParent: self)
    }

    // Returning ContentViewController present at the index of the slider
    private  func viewControllerAtIndex(_ index: Int) -> TutorialViewController {
        // If there is no ContentViewController return empty refrences
        guard self.pageDescription.count != 0, index < self.pageDescription.count else {
            return TutorialViewController()
        }

        // Otherwise instantiating a ContentViewController
        guard let tutorialViewController = self.storyboard?.instantiateViewController(withIdentifier: "tutorialVC") as? TutorialViewController else {
            return TutorialViewController()
        }

        // Adding text and media to the ContentViewController instance
        tutorialViewController.imageFile = self.pageMedia[index]
        tutorialViewController.titleText = self.pageDescription[index]
        tutorialViewController.pageIndex = index

        // return the ContentViewController instance refrence
        return tutorialViewController
    }
}

/// MARK: - Page View Controller Data Source
extension WalkThroughViewController : UIPageViewControllerDataSource {

    func pageViewController(_ pageViewController: UIPageViewController, viewControllerBefore viewController: UIViewController) -> UIViewController? {

        guard let tutorialViewController = viewController as? TutorialViewController else {
            return nil
        }
        guard tutorialViewController.pageIndex != 0, tutorialViewController.pageIndex != NSNotFound else {
            return nil
        }
        let index = tutorialViewController.pageIndex - 1
        return self.viewControllerAtIndex(index)
    }

    func pageViewController(_ pageViewController: UIPageViewController, viewControllerAfter viewController: UIViewController) -> UIViewController? {

        guard
            let tutorialViewController = viewController as? TutorialViewController,
            tutorialViewController.pageIndex != NSNotFound else {
                return nil
        }
        let index = tutorialViewController.pageIndex + 1
        guard index != self.pageDescription.count else {
            return nil
        }
        return self.viewControllerAtIndex(index)
    }

    func presentationCount(for pageViewController: UIPageViewController) -> Int {
        return self.pageDescription.count
    }

    func presentationIndex(for pageViewController: UIPageViewController) -> Int {
        return 0
    }
}
