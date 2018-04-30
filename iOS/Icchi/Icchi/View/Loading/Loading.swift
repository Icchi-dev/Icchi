
import UIKit

class Loading: UIView {
    
    @IBOutlet private weak var loadingImageView: UIImageView!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        self.animate()
    }
    
    private func animate() {
        
        let animation = CABasicAnimation(keyPath: "transform.rotation.z")
        animation.toValue = NSNumber(value: 2 * Double.pi)
        animation.duration = 0.8
        animation.repeatCount = .infinity
        loadingImageView.layer.add(animation, forKey: "rotate")
    }
    
    class func start() {
        
        guard let window = UIApplication.shared.keyWindow else {
            return
        }
        if !(window.subviews.compactMap { $0 as? Loading }).isEmpty {
            return
        }
        guard let loadingView = UINib(nibName: "Loading", bundle: nil).instantiate(withOwner: self, options: nil).first as? Loading else {
            return
        }
        window.addSubview(loadingView)
        loadingView.frame = CGRect(origin: .zero, size: window.frame.size)
    }
    
    class func stop() {
        
        guard let window = UIApplication.shared.keyWindow else {
            return
        }
        let loadingViews = window.subviews.compactMap { $0 as? Loading }
        loadingViews.forEach { $0.removeFromSuperview() }
    }
}
